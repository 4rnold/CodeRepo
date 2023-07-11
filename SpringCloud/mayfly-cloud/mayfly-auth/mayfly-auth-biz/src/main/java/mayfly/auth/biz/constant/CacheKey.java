package mayfly.auth.biz.constant;

/**
 * @author meilin.huang
 * @date 2022-03-21 15:42
 */
public class CacheKey {
    /**
     * token过期时间: {@value }
     */
    public static final Integer SESSION_EXPIRE_TIME = 120;

    /**
     * 验证码过期时间: {@value }
     */
    public static final Integer CAPTCHA_EXPIRE_TIME = 3;


    public static final String CAPTCHA_KEY = "captcha:{uuid}";

    /**
     * 账号token key
     */
    public static final String ACCOUNT_TOKEN_KEY = "account:token:{token}";
}
