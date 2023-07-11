package mayfly.auth.biz.contoller;

import mayfly.auth.biz.contoller.form.LoginForm;
import mayfly.auth.biz.contoller.vo.LoginSuccessVO;
import mayfly.auth.biz.service.CaptchaService;
import mayfly.auth.biz.service.PermissionService;
import mayfly.core.enums.EnableDisableEnum;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.Response2Result;
import mayfly.core.util.DigestUtils;
import mayfly.core.web.WebUtils;
import mayfly.sys.api.AccountRemoteService;
import mayfly.sys.api.model.query.AccountQueryDTO;
import mayfly.sys.api.model.req.UpdateAccountLoginReqDTO;
import mayfly.sys.api.model.res.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @date 2022-03-19 20:31
 */
@Response2Result
@RestController
public class LoginController {

    @Autowired
    private AccountRemoteService accountRemoteService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private CaptchaService captchaService;

    @Log(value = "账号登录", resLevel = Log.Level.NONE)
    @PostMapping("/login")
    public LoginSuccessVO login(@Valid @RequestBody LoginForm loginForm) {
        BizAssert.isTrue(captchaService.checkCaptcha(loginForm.getUuid(), loginForm.getCaptcha()), "验证码错误");

        AccountDTO account = accountRemoteService.getByQuery(new AccountQueryDTO().setUsername(loginForm.getUsername()))
                .tryGet();
        BizAssert.notNull(account, "账号不存在");
        BizAssert.equals(DigestUtils.md5DigestAsHex(loginForm.getPassword()), account.getPassword(), "用户名或密码错误");
        BizAssert.equals(account.getStatus(), EnableDisableEnum.ENABLE.getValue(), "该账号已被禁用");

        LoginSuccessVO lsv = permissionService.saveIdAndPermission(account);
        accountRemoteService.updateLogin(new UpdateAccountLoginReqDTO()
                .setAccountId(lsv.getAccount().getId())
                .setLoginIp(WebUtils.getRequestIp())
                .setLoginTime(LocalDateTime.now())).tryGet();
        return lsv;
    }

    @Log("账号登出")
    @PostMapping("/logout/{token}")
    public void logout(@PathVariable String token) {
        permissionService.removeToken(token);
    }
}
