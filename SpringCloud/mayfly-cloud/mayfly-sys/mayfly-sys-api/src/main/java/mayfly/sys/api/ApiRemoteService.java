package mayfly.sys.api;

import mayfly.core.model.result.Result;
import mayfly.sys.api.constant.SysConst;
import mayfly.sys.api.model.query.ApiQueryDTO;
import mayfly.sys.api.model.query.ServiceQueryDTO;
import mayfly.sys.api.model.res.ApiDTO;
import mayfly.sys.api.model.res.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-30 15:03
 */
@FeignClient(SysConst.PROJECT_NAME)
public interface ApiRemoteService {

    /**
     * 获取服务列表信息
     *
     * @param queryDTO 服务查询条件dto
     * @return 服务列表
     */
    @PostMapping("/remote/get-services")
    Result<List<ServiceDTO>> listServiceByQuery(@RequestBody ServiceQueryDTO queryDTO);

    /**
     * 获取api列表信息
     *
     * @param queryDTO api列表查询条件实体
     * @return api列表
     */
    @PostMapping("/remote/get-apis")
    Result<List<ApiDTO>> listApiByQuery(@RequestBody ApiQueryDTO queryDTO);
}
