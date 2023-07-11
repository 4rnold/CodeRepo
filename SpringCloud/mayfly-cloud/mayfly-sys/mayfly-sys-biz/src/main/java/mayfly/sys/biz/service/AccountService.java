package mayfly.sys.biz.service;


import mayfly.core.base.service.BaseService;
import mayfly.core.model.result.PageResult;
import mayfly.sys.biz.controller.form.AccountForm;
import mayfly.sys.biz.controller.query.AccountQuery;
import mayfly.sys.biz.controller.vo.AccountVO;
import mayfly.sys.biz.entity.AccountDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:56
 */
public interface AccountService extends BaseService<Long, AccountDO> {

    PageResult<AccountVO> listByQuery(AccountQuery query);

    void create(AccountForm accountForm);
}
