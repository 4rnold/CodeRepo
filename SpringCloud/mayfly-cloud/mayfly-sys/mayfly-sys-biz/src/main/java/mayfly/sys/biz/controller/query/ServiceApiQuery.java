package mayfly.sys.biz.controller.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayfly.core.model.PageQuery;

/**
 * @author meilin.huang
 * @date 2022-03-30 17:31
 */
@Getter
@Setter
@ToString
public class ServiceApiQuery extends PageQuery {

    private String serviceId;

    private String serviceCode;

    private String updateTime;
}
