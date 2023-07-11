package mayfly.sys.api;

import mayfly.core.model.result.Result;
import mayfly.sys.api.constant.SysConst;
import mayfly.sys.api.model.query.AccountQueryDTO;
import mayfly.sys.api.model.req.UpdateAccountLoginReqDTO;
import mayfly.sys.api.model.res.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author meilin.huang
 * @date 2022-03-19 19:47
 */
@FeignClient(SysConst.PROJECT_NAME)
public interface AccountRemoteService {

    /**
     * 根据查询条件获取账号信息
     *
     * @param query query
     * @return 账号信息
     */
    @PostMapping("/remote/accounts")
    Result<AccountDTO> getByQuery(@RequestBody AccountQueryDTO query);

    /**
     * 更新账号登录后信息
     *
     * @param req req
     * @return result
     */
    @PostMapping("/remote/accounts/update-login")
    Result<Void> updateLogin(@RequestBody UpdateAccountLoginReqDTO req);
}
