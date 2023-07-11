package mayfly.sys.biz.controller.form;

import lombok.Data;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.log.annotation.LogChange;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.sys.api.enums.ApiCodeTypeEnum;
import mayfly.sys.api.enums.HttpMethodEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:39
 */
@LogChange
@Data
public class ServiceApiForm {

    @NotNull(message = "服务id不能为空")
    private Long serviceId;

    @NotEmpty(message = "api名称不能为空")
    private String name;

    @NotNull(message = "code类型不能为空")
    @EnumValue(ApiCodeTypeEnum.class)
    private Integer codeType;

    @NotEmpty(message = "api code不能为空")
    private String code;

    @NotEmpty(message = "请求方法不能为空")
    @EnumValue(HttpMethodEnum.class)
    private String method;

    @NotEmpty(message = "请求uri不能为空")
    private String uri;

    @NotNull(message = "状态不能为空")
    @EnumValue(EnableDisableEnum.class)
    private Integer status;
}
