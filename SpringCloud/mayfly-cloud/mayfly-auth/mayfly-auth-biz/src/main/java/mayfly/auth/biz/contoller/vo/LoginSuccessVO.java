package mayfly.auth.biz.contoller.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import mayfly.sys.api.model.res.ResourceDTO;

import java.util.List;
import java.util.Set;

/**
 * @author meilin.huang
 * @date 2022-03-19 20:37
 */
@Data
@Accessors(chain = true)
public class LoginSuccessVO {

    private String token;

    private AccountVO account;

    private List<ResourceDTO> menus;

    private Set<String> codes;
}
