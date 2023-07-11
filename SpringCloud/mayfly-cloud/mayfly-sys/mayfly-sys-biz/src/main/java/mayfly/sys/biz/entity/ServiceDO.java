package mayfly.sys.biz.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:13
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table("t_service")
public class ServiceDO extends BaseDO {

    private String name;

    private String code;

    private Integer status;

    private Integer isDeleted;
}
