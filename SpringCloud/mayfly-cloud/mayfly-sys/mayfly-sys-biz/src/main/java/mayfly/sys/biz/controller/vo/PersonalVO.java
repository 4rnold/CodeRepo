package mayfly.sys.biz.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author meilin.huang
 * @date 2022-04-12 09:46
 */
@Accessors(chain = true)
@Data
public class PersonalVO {

    private List<AccountRoleVO> roles;
}
