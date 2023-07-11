package mayfly.sys.biz.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.Log;
import mayfly.sys.biz.entity.AccountRoleDO;
import mayfly.sys.biz.entity.RoleDO;
import mayfly.sys.biz.entity.RoleResourceDO;
import mayfly.sys.biz.mapper.RoleMapper;
import mayfly.sys.biz.service.AccountRoleService;
import mayfly.sys.biz.service.OperationLogService;
import mayfly.sys.biz.service.RoleResourceService;
import mayfly.sys.biz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
@Log("角色管理:")
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Long, RoleDO> implements RoleService {

    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private OperationLogService logService;

    @Override
    public Long create(RoleDO role) {
        BizAssert.equals(countByCondition(new RoleDO().setCode(role.getCode())), 0L, "该角色标识已存在");
        role.setStatus(EnableDisableEnum.ENABLE.getValue());
        insert(role);
        return role.getId();
    }

    @Override
    public void update(RoleDO role) {
        RoleDO old = getById(role.getId());
        BizAssert.notNull(old, "角色不存在");
        updateByIdSelective(role);
        logService.asyncUpdateLog("修改角色", role, old);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        RoleDO role = getById(id);
        BizAssert.notNull(role, "角色不存在");
        // 删除角色关联的用户角色信息
        accountRoleService.deleteByCondition(new AccountRoleDO().setRoleId(id));
        // 删除角色关联的资源信息
        roleResourceService.deleteByCondition(new RoleResourceDO().setRoleId(id));
        deleteById(id);
        logService.asyncDeleteLog("删除角色", role);
    }
}
