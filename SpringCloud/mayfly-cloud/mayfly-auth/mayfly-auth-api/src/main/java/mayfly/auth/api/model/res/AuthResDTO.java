package mayfly.auth.api.model.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.permission.LoginAccount;

/**
 * @author meilin.huang
 * @date 2022-03-25 11:13
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AuthResDTO {

    /**
     * 登录账号信息
     */
    private LoginAccount loginAccount;
}
