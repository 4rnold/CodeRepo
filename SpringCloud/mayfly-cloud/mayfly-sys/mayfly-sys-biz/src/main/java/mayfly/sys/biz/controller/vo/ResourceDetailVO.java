package mayfly.sys.biz.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayfly.core.model.BaseDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-27 10:52
 */
@Getter
@Setter
@ToString
public class ResourceDetailVO extends BaseDO {

    private Integer type;

    private String name;

    private String code;

    private String meta;

    private Integer status;

    private Integer weight;
}
