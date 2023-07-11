package mayfly.auth.biz.service.impl;

import mayfly.auth.biz.constant.CacheKey;
import mayfly.auth.biz.contoller.vo.CaptchaVO;
import mayfly.auth.biz.service.CaptchaService;
import mayfly.core.captcha.ArithmeticCaptcha;
import mayfly.core.captcha.CaptchaBuilder;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @date 2022-03-21 15:59
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public CaptchaVO generateCaptcha() {
        ArithmeticCaptcha ac = CaptchaBuilder.<ArithmeticCaptcha>newArithmeticBuilder()
                .len(3).build();
        String text = ac.text();
        String uuid = UUIDUtils.generateUUID();
        String key = BracePlaceholder.resolve(CacheKey.CAPTCHA_KEY, uuid);
        redisTemplate.opsForValue().set(key, text, CacheKey.CAPTCHA_EXPIRE_TIME, TimeUnit.MINUTES);
        return new CaptchaVO(uuid, ac.toBase64());
    }

    @Override
    public boolean checkCaptcha(String uuid, String captcha) {
        String key = BracePlaceholder.resolve(CacheKey.CAPTCHA_KEY, uuid);
        String text = (String) redisTemplate.opsForValue().get(key);
        BizAssert.notNull(text, "验证码过期");
        if (!captcha.equalsIgnoreCase(text)) {
            return false;
        }
        redisTemplate.delete(key);
        return true;
    }
}
