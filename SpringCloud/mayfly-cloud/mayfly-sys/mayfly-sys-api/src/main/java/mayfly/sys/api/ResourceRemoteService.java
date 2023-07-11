package mayfly.sys.api;

import mayfly.core.model.result.Result;
import mayfly.sys.api.constant.SysConst;
import mayfly.sys.api.model.res.ResourceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-03-20 21:09
 */
@FeignClient(SysConst.PROJECT_NAME)
public interface ResourceRemoteService {

    /**
     * 根据账号id获取资源列表
     *
     * @param accountId 账号id
     * @return 资源列表
     */
    @GetMapping("/remote/accounts/{accountId}/resources")
    Result<List<ResourceDTO>> listByAccountId(@PathVariable("accountId") Long accountId);
}
