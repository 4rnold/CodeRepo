package mayfly.auth.biz.service;

import mayfly.auth.biz.contoller.vo.CaptchaVO;

/**
 * @author meilin.huang
 * @date 2022-03-21 15:57
 */
public interface CaptchaService {

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    CaptchaVO generateCaptcha();

    /**
     * 校验验证码
     *
     * @param uuid    uuid
     * @param captcha 验证码
     * @return 是否正确
     */
    boolean checkCaptcha(String uuid, String captcha);
}
