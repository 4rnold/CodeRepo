package mayfly.sys.biz.controller.form;

import lombok.Data;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.log.annotation.LogChange;
import mayfly.core.validation.annotation.EnumValue;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author meilin.huang
 * @date 2022-03-28 21:39
 */
@LogChange
@Data
public class ServiceForm {

    @NotEmpty(message = "服务code不能为空")
    private String code;

    @NotEmpty(message = "服务名不能为空")
    private String name;

    @NotNull(message = "状态不能为空")
    @EnumValue(EnableDisableEnum.class)
    private Integer status;
}
