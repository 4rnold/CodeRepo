package mayfly.auth.biz.permission;

import lombok.extern.slf4j.Slf4j;
import mayfly.auth.biz.config.ApiConfig;
import mayfly.core.enums.BooleanEnum;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.thread.ScheduleUtils;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.DateUtils;
import mayfly.core.util.PathUtils;
import mayfly.sys.api.ApiRemoteService;
import mayfly.sys.api.model.query.ApiQueryDTO;
import mayfly.sys.api.model.query.ServiceQueryDTO;
import mayfly.sys.api.model.res.ApiDTO;
import mayfly.sys.api.model.res.ServiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限码校验服务.
 * <p>服务启动时会全量获取已配置的服务及api信息.
 * <p>后续若服务与api有更新则会增量地根据最后更新时间去获取修改后的信息并做相应处理
 *
 * @author meilin.huang
 * @date 2022-03-30 19:49
 */
@Slf4j
@Service
public class ApiCheckService implements ApplicationRunner {

    @Autowired
    private ApiRemoteService apiRemoteService;

    @Autowired
    private ApiConfig apiConfig;

    /**
     * 更新api的时间周期,单位分钟
     */
    private final int updateApiPeriod = 2;

    /**
     * 是否全量获取服务与api信息
     */
    private boolean completed = true;

    /**
     * 服务与api信息缓存，key -> 服务code,  value -> (key -> api uri, value -> api信息)
     */
    private static final Map<String, Map<String, Api>> SERVICE_API_CACHE = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) {
        scheduleUpdateApi();
    }

    /**
     * 是否可以访问指定服务的请求路径
     *
     * @param account     登录账号信息
     * @param serviceCode 服务code
     * @param requestUri  请求uri
     * @return 是否可访问
     */
    public boolean canAccess(SysLoginAccount account, String serviceCode, String requestUri) {
        if (!apiConfig.isCheck()) {
            return true;
        }
        Map<String, Api> apiMap = SERVICE_API_CACHE.get(serviceCode);
        BizAssert.notEmpty(apiMap, () -> String.format("[%s]服务未配置或已被禁用", serviceCode));
        for (Map.Entry<String, Api> entry : apiMap.entrySet()) {
            String uri = entry.getKey();
            // 如果路径匹配，则判断该api的code是否存在于用户的权限code列表中
            if (PathUtils.match(uri, requestUri)) {
                Api api = entry.getValue();
                BizAssert.equals(api.getStatus(), EnableDisableEnum.ENABLE.getValue(), "该api已被禁用");
                return account.hasPermission(api.getCode());
            }
        }
        // api请求地址没有配置，则默认为不需要鉴权api
        return true;
    }

    /**
     * 定时获取增量修改的api信息
     */
    private void scheduleUpdateApi() {
        log.info("应用启动完成...开始获取已配置的服务及其对应的api信息");
        ScheduleUtils.scheduleAtFixedRate("updateApi", () -> {
            log.debug("开始获取近期更新的服务及api信息, 是否全量: {}", completed);
            try {
                updateServiceApiCache();
                if (completed) {
                    completed = false;
                }
                log.debug("获取近期更新的服务及api信息结束");
            } catch (Exception e) {
                log.error("获取近期更新的服务及其api信息失败", e);
            }
        }, 0, updateApiPeriod, TimeUnit.MINUTES);
    }

    /**
     * 更新服务与api信息
     */
    private void updateServiceApiCache() {
        String lastUpdateTimeStr = null;
        // 非全量获取，则设置最后更新时间
        if (!completed) {
            lastUpdateTimeStr = DateUtils.defaultFormat(LocalDateTime.now().minusMinutes(updateApiPeriod + 1));
        }

        List<ServiceDTO> services = apiRemoteService.listServiceByQuery(new ServiceQueryDTO().setUpdateTime(lastUpdateTimeStr)).tryGet();
        if (CollectionUtils.isEmpty(services)) {
            return;
        }
        // 全量更新，则直接赋值即可
        if (completed) {
            log.debug("全量获取到的服务列表: {}", services);
            for (ServiceDTO service : services) {
                if (BooleanEnum.isTrue(service.getIsDeleted()) || !EnableDisableEnum.ENABLE.getValue().equals(service.getStatus())) {
                    continue;
                }
                String code = service.getCode();
                List<ApiDTO> apis = apiRemoteService.listApiByQuery(new ApiQueryDTO().setServiceCode(code)).tryGet();
                SERVICE_API_CACHE.put(code, toApiMap(apis));
            }
            return;
        }

        log.info("增量获取到最近更新的服务列表: {}", services);
        for (ServiceDTO service : services) {
            String code = service.getCode();
            boolean exist = SERVICE_API_CACHE.containsKey(code);

            // 服务被删除，并且缓存中存在该信息，则删除
            if ((BooleanEnum.isTrue(service.getIsDeleted()) || !EnableDisableEnum.ENABLE.getValue().equals(service.getStatus())) && exist) {
                log.info("删除服务 --> [{}]", code);
                SERVICE_API_CACHE.remove(code);
                continue;
            }
            if (!exist) {
                // 如果不存在，则全量获取该服务对应的api信息，并进行缓存
                List<ApiDTO> apis = apiRemoteService.listApiByQuery(new ApiQueryDTO().setServiceCode(code)).tryGet();
                SERVICE_API_CACHE.put(code, toApiMap(apis));
                continue;
            }

            // 获取最近有更新的api信息
            List<ApiDTO> apis = apiRemoteService.listApiByQuery(new ApiQueryDTO()
                    .setServiceCode(code)
                    .setUpdateTime(lastUpdateTimeStr)).tryGet();
            if (CollectionUtils.isEmpty(apis)) {
                continue;
            }
            Map<String, Api> oldApis = SERVICE_API_CACHE.get(code);
            for (ApiDTO a : apis) {
                Api api = toApi(a);
                String uri = a.getUri();
                boolean apiExists = oldApis.containsKey(uri);
                if (BooleanEnum.isTrue(a.getIsDeleted()) && apiExists) {
                    log.info("删除api --> [{}] [{}]", a.getCode(), a.getUri());
                    oldApis.remove(uri);
                    continue;
                }
                log.info("更新或新增api --> [{}] [{}]", a.getCode(), a.getUri());
                oldApis.put(api.getUri(), api);
            }
        }
    }

    private Map<String, Api> toApiMap(List<ApiDTO> apis) {
        return apis.stream().filter(a -> BooleanEnum.isFalse(a.getIsDeleted()))
                .map(this::toApi)
                .collect(Collectors.toMap(Api::getUri, api -> api));
    }

    private Api toApi(ApiDTO dto) {
        return new Api().setCode(dto.getCode())
                .setUri(String.format("%s:%s", dto.getMethod(), dto.getUri()))
                .setStatus(dto.getStatus());
    }
}
