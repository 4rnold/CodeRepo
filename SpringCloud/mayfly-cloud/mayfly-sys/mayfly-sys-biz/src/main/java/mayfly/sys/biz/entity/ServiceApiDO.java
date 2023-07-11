package mayfly.sys.biz.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:17
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Table("t_service_api")
public class ServiceApiDO extends BaseDO {

    /**
     * 服务id {@link ServiceDO#getId()}
     */
    private Long serviceId;

    private String serviceCode;

    private String name;

    private Integer codeType;

    private String code;

    private String method;

    private String uri;

    private Integer status;

    private Integer isDeleted;
}
