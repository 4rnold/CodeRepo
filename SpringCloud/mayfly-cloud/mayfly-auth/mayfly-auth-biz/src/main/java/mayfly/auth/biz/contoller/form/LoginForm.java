package mayfly.auth.biz.contoller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author meilin.huang
 * @date 2022-03-19 20:31
 */
@Getter
@Setter
public class LoginForm {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @NotBlank(message = "验证码uuid不能为空")
    private String uuid;

    @Override
    public String toString() {
        return "AccountLoginForm{" +
                "username='" + username + '\'' +
                ", password=' ******" + '\'' +
                ", captcha='******" + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
