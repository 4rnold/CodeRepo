package mayfly.sys.biz.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.model.result.PageResult;
import mayfly.core.util.DigestUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.biz.controller.form.AccountForm;
import mayfly.sys.biz.controller.query.AccountQuery;
import mayfly.sys.biz.controller.vo.AccountVO;
import mayfly.sys.biz.entity.AccountDO;
import mayfly.sys.biz.mapper.AccountMapper;
import mayfly.sys.biz.service.AccountRoleService;
import mayfly.sys.biz.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:57
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountMapper, Long, AccountDO> implements AccountService {

    @Autowired
    private AccountRoleService accountRoleService;


    @Override
    public PageResult<AccountVO> listByQuery(AccountQuery query) {
        return PageResult.withPageHelper(query, () -> mapper.selectByQuery(query), AccountVO.class);
    }

    @Override
    public void create(AccountForm accountForm) {
        BizAssert.equals(countByCondition(new AccountDO().setUsername(accountForm.getUsername())), 0L,
                "该用户名已存在");
        AccountDO account = BeanUtils.copy(accountForm, AccountDO.class);
        account.setPassword(DigestUtils.md5DigestAsHex(accountForm.getPassword()));
        // 默认启用状态
        account.setStatus(EnableDisableEnum.ENABLE.getValue());
        insert(account);
    }
}
