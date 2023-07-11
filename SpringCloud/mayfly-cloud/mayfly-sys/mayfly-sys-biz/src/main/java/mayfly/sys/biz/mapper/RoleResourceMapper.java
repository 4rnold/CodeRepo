package mayfly.sys.biz.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.biz.controller.vo.RoleResourceVO;
import mayfly.sys.biz.entity.RoleResourceDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:15 PM
 */
public interface RoleResourceMapper extends BaseMapper<Long, RoleResourceDO> {

    @Select("select rr.creator AS creator, rr.create_time AS createTime, rr.resource_id AS resourceId, r.pid AS resourcePid, " +
            "r.name AS resourceName, r.type AS type, r.status AS status " +
            "FROM t_role_resource rr JOIN t_resource r ON rr.resource_id = r.id " +
            "WHERE rr.role_id = #{roleId} " +
            "ORDER BY r.pid ASC, r.weight ASC")
    List<RoleResourceVO> selectResourceByRoleId(@Param("roleId") Long roleId);
}
