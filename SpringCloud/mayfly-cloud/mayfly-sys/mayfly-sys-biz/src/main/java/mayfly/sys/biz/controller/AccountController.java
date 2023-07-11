package mayfly.sys.biz.controller;

import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.PageResult;
import mayfly.core.model.result.Response2Result;
import mayfly.core.util.TreeUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.util.enums.EnumUtils;
import mayfly.sys.biz.controller.form.AccountForm;
import mayfly.sys.biz.controller.form.RoleUserForm;
import mayfly.sys.biz.controller.query.AccountQuery;
import mayfly.sys.biz.controller.vo.AccountRoleVO;
import mayfly.sys.biz.controller.vo.AccountVO;
import mayfly.sys.biz.controller.vo.ResourceListVO;
import mayfly.sys.biz.entity.AccountDO;
import mayfly.sys.biz.service.AccountRoleService;
import mayfly.sys.biz.service.AccountService;
import mayfly.sys.biz.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 管理员控制器
 *
 * @author hml
 * @date 2018/6/27 下午4:44
 */
@Response2Result
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private ResourceService resourceService;

    @GetMapping()
    public PageResult<AccountVO> list(AccountQuery accountQuery) {
        return accountService.listByQuery(accountQuery);
    }

    @Log("新建账号")
    @PostMapping()
    public void save(@Valid @RequestBody AccountForm accountForm) {
        accountService.create(accountForm);
    }

    @Log("更新账号")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody AccountForm accountForm) {
        accountService.create(accountForm);
    }

    @Log("修改账号状态")
    @PutMapping("/{id}/{status}")
    public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        BizAssert.isTrue(EnumUtils.isExist(EnableDisableEnum.values(), status), "状态值错误");
        AccountDO a = new AccountDO().setStatus(status);
        a.setId(id);
        accountService.updateByIdSelective(a);
    }

    @Log("删除账号")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accountService.deleteById(id);
    }

    @GetMapping("/{id}/roleIds")
    public List<Long> roleIds(@PathVariable Long id) {
        return accountRoleService.listRoleIdByAccountId(id);
    }

    @GetMapping("/{id}/roles")
    public List<AccountRoleVO> roles(@PathVariable Long id) {
        return BeanUtils.copy(accountRoleService.listRoleByAccountId(id), AccountRoleVO.class);
    }

    @GetMapping("/{id}/resources")
    public List<ResourceListVO> resources(@PathVariable Long id) {
        return TreeUtils.generateTrees(resourceService.listByAccountId(id));
    }

    @PostMapping("/{id}/roles")
    public void saveRoles(@PathVariable Long id, @RequestBody RoleUserForm adminForm) {
        List<Long> ids;
        try {
            ids = Stream.of(adminForm.getRoleIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            throw BizAssert.newException("roleIds参数错误！");
        }
        accountRoleService.saveRoles(id, ids);
    }
}
