package mayfly.sys.biz.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

import java.io.Serializable;
import java.util.List;

/**
 * 角色
 *
 * @author hml
 * @date 2018/6/27 下午2:03
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table("t_role")
public class RoleDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 5311816841875852758L;

    private String name;

    private String code;

    private String remark;

    private Integer status;

    @NoColumn
    private List<ResourceDO> resources;
}
