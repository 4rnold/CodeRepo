package mayfly.sys.biz.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.biz.entity.RoleDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
public interface RoleService extends BaseService<Long, RoleDO> {

    Long create(RoleDO role);

    /**
     * 更新角色
     *
     * @param role 角色
     */
    void update(RoleDO role);

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    void delete(Long id);
}
