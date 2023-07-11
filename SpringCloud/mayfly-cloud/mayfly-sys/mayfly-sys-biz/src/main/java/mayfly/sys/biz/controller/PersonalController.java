package mayfly.sys.biz.controller;

import mayfly.core.model.result.Response2Result;
import mayfly.core.permission.LoginAccount;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.biz.controller.vo.AccountRoleVO;
import mayfly.sys.biz.controller.vo.PersonalVO;
import mayfly.sys.biz.service.AccountRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @date 2022-04-12 09:42
 */
@Response2Result
@RestController
public class PersonalController {

    @Autowired
    private AccountRoleService accountRoleService;

    @GetMapping("/personal")
    public PersonalVO getPersonal() {
        return new PersonalVO()
                .setRoles(BeanUtils.copy(accountRoleService.listRoleByAccountId(LoginAccount.getLoginAccountId()), AccountRoleVO.class));
    }
}
