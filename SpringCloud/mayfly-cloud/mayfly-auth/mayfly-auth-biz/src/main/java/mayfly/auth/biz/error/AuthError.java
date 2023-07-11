package mayfly.auth.biz.error;

import mayfly.core.model.result.CodeMessage;

/**
 * @author meilin.huang
 * @date 2022-03-19 20:47
 */
public enum AuthError implements CodeMessage {

    /**
     * 认证服务断言失败错误码
     */
    ASSERT_ERROR("AUTH001", "认证服务断言失败"),

    /**
     * 未登录错误
     */
    NO_LOGIN("AUTH002", "登录失效，请重新登录"),

    /**
     * 非法访问
     */
    UNAUTH_ACCESS("AUTH003", "非法访问");

    private final String code;

    private final String message;

    AuthError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}