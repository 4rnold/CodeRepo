package mayfly.auth.biz.contoller;

import mayfly.auth.biz.contoller.vo.CaptchaVO;
import mayfly.auth.biz.service.CaptchaService;
import mayfly.core.model.result.Response2Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @date 2022-03-21 15:55
 */
@RestController
@Response2Result
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/captcha")
    public CaptchaVO captcha() {
        return captchaService.generateCaptcha();
    }
}
