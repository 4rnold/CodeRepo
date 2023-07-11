package mayfly.sys.biz.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.CollectionUtils;
import mayfly.sys.biz.entity.AccountRoleDO;
import mayfly.sys.biz.entity.RoleDO;
import mayfly.sys.biz.mapper.AccountRoleMapper;
import mayfly.sys.biz.service.AccountRoleService;
import mayfly.sys.biz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:13
 */
@Service
public class AccountRoleServiceImpl extends BaseServiceImpl<AccountRoleMapper, Long, AccountRoleDO> implements AccountRoleService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<RoleDO> listRoleByAccountId(Long accountId) {
        return mapper.selectRoleByAccountId(accountId);
    }

    @Override
    public List<Long> listRoleIdByAccountId(Long accountId) {
        return listByCondition(new AccountRoleDO().setAccountId(accountId)).stream().map(AccountRoleDO::getRoleId)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRoles(Long accountId, List<Long> roleIds) {
        List<Long> oldRoles = listRoleIdByAccountId(accountId);

        //和之前存的角色列表id比较，哪些是新增，哪些是修改以及不变的
        CollectionUtils.CompareResult<Long> compareResult = CollectionUtils
                .compare(new HashSet<>(roleIds), new HashSet<>(oldRoles));
        Set<Long> delIds = compareResult.getDelValue();
        Set<Long> addIds = compareResult.getAddValue();

        delIds.forEach(r -> {
            deleteByCondition(new AccountRoleDO().setAccountId(accountId).setRoleId(r));
        });

        if (CollectionUtils.isEmpty(addIds)) {
            return;
        }
        List<AccountRoleDO> ars = new ArrayList<>(addIds.size());
        // 校验资源id正确性，及保存新增的资源id
        BizAssert.equals(roleService.listByIdIn(new ArrayList<>(addIds)).size(), addIds.size(), "存在错误角色id");
        for (Long id : addIds) {
            AccountRoleDO ru = new AccountRoleDO().setRoleId(id).setAccountId(accountId);
            ru.autoSetBaseInfo();
            ars.add(ru);
        }
        batchInsert(ars);
    }
}
