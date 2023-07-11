package mayfly.sys.biz.remote;

import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.api.AccountRemoteService;
import mayfly.sys.api.model.query.AccountQueryDTO;
import mayfly.sys.api.model.req.UpdateAccountLoginReqDTO;
import mayfly.sys.api.model.res.AccountDTO;
import mayfly.sys.biz.entity.AccountDO;
import mayfly.sys.biz.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @date 2022-03-19 19:49
 */
@RestController
public class AccountRemoteServiceImpl implements AccountRemoteService {

    @Autowired
    private AccountService accountService;

    @Log(value = "查询账号信息", resLevel = Log.Level.NONE)
    @Override
    public Result<AccountDTO> getByQuery(AccountQueryDTO query) {
        return Result.success(BeanUtils.copy(accountService.getByCondition(BeanUtils.copy(query, AccountDO.class))
                , AccountDTO.class));
    }

    @Override
    public Result<Void> updateLogin(UpdateAccountLoginReqDTO req) {
        AccountDO update = new AccountDO().setLastLoginIp(req.getLoginIp())
                .setLastLoginTime(req.getLoginTime());
        update.setId(req.getAccountId());
        accountService.updateByIdSelective(update);
        return Result.success();
    }
}
