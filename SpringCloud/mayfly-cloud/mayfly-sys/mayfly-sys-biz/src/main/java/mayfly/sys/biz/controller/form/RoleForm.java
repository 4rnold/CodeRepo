package mayfly.sys.biz.controller.form;

import lombok.Data;
import mayfly.core.log.annotation.LogChange;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-20 9:35 AM
 */
@Data
public class RoleForm {

    private Long id;

    @LogChange(name = "角色名")
    @NotBlank
    private String name;

    @NotEmpty(message = "角色标识不能为空")
    private String code;

    @NotBlank
    private String remark;

    private String resourceIds;
}
