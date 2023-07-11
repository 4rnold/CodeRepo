package mayfly.auth.biz.service.impl;

import lombok.extern.slf4j.Slf4j;
import mayfly.auth.api.model.req.AuthDTO;
import mayfly.auth.api.model.res.AuthResDTO;
import mayfly.auth.biz.constant.CacheKey;
import mayfly.auth.biz.contoller.vo.AccountVO;
import mayfly.auth.biz.contoller.vo.LoginSuccessVO;
import mayfly.auth.biz.error.AuthError;
import mayfly.auth.biz.permission.ApiCheckService;
import mayfly.auth.biz.permission.SysLoginAccount;
import mayfly.auth.biz.service.PermissionService;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.permission.LoginAccount;
import mayfly.core.permission.registry.LoginAccountRegistryHandler;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.util.StringUtils;
import mayfly.core.util.TreeUtils;
import mayfly.core.util.UUIDUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.api.ResourceRemoteService;
import mayfly.sys.api.enums.ResourceTypeEnum;
import mayfly.sys.api.model.res.AccountDTO;
import mayfly.sys.api.model.res.ResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 *
 * @author hml
 * @date 2018/6/26 上午9:49
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ResourceRemoteService resourceRemoteService;
    @Autowired
    private ApiCheckService apiCheckService;

    /**
     * 权限缓存处理器
     */
    private final LoginAccountRegistryHandler loginAccountRegistryHandler = LoginAccountRegistryHandler.of(this);


    @Override
    public LoginSuccessVO saveIdAndPermission(AccountDTO account) {
        // 获取账号拥有的资源列表
        List<ResourceDTO> resources = resourceRemoteService.listByAccountId(account.getId()).tryGet();

        Long id = account.getId();
        String token = UUIDUtils.generateUUID();
        // 菜单列表
        List<ResourceDTO> menus = new ArrayList<>();
        // 含有权限code的列表
        List<ResourceDTO> codes = new ArrayList<>();
        for (ResourceDTO r : resources) {
            if (Objects.equals(r.getType(), ResourceTypeEnum.MENU.getValue())) {
                menus.add(r);
            } else {
                if (!StringUtils.isEmpty(r.getCode())) {
                    codes.add(r);
                }
            }
        }

        // 获取所有含有权限code的资源，如果权限被禁用则不返回
        Set<String> permissionCodes = codes.stream()
                .filter(c -> c.getStatus().equals(EnableDisableEnum.ENABLE.getValue()))
                .map(ResourceDTO::getCode).collect(Collectors.toSet());

        // 保存系统登录账号信息
        LoginAccount loginAccount = LoginAccount.create(account.getId()).username(account.getUsername())
                .to(SysLoginAccount.class)
                .permissions(permissionCodes);

        // 缓存账号与资源信息
        save(token, loginAccount, CacheKey.SESSION_EXPIRE_TIME, TimeUnit.MINUTES);

        return new LoginSuccessVO().setAccount(BeanUtils.copy(account, AccountVO.class))
                .setToken(token)
                .setMenus(TreeUtils.generateTrees(menus))
                .setCodes(permissionCodes);
    }

    @Override
    public void removeToken(String token) {
        loginAccountRegistryHandler.removeLoginAccount(token);
    }

    @Override
    public AuthResDTO auth(AuthDTO req) {
        SysLoginAccount loginAccount = getLoginAccount(req.getToken());
        BizAssert.notNull(loginAccount, AuthError.NO_LOGIN);
        if (!apiCheckService.canAccess(loginAccount, req.getService(), req.getUri())) {
            log.warn("用户: {}, 无权访问: {} --> {}", loginAccount.getUsername(), req.getService(), req.getUri());
            throw BizAssert.newException(AuthError.UNAUTH_ACCESS);
        }
        return new AuthResDTO().setLoginAccount(loginAccount);
    }

    //------------------------------------------------------------
    //  LoginAccountRegistry  接口实现类
    //------------------------------------------------------------

    @SuppressWarnings("all")
    @Override
    public void save(String token, LoginAccount loginAccount, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(BracePlaceholder.resolveByObject(CacheKey.ACCOUNT_TOKEN_KEY, token), loginAccount, time, timeUnit);
    }

    @Override
    public SysLoginAccount getLoginAccount(String token) {
        return StringUtils.isEmpty(token) ? null : (SysLoginAccount) redisTemplate.opsForValue().get(BracePlaceholder.resolveByObject(CacheKey.ACCOUNT_TOKEN_KEY, token));
    }

    @SuppressWarnings("all")
    @Override
    public void delete(String token) {
        redisTemplate.delete(BracePlaceholder.resolveByObject(CacheKey.ACCOUNT_TOKEN_KEY, token));
    }
}
