package mayfly.gateway.error;

import mayfly.core.model.result.CodeMessage;

/**
 * @author meilin.huang
 * @date 2022-03-16 15:37
 */
public enum GatewayError implements CodeMessage {
    /**
     * 断言错误
     */
    ASSERT_ERROR("GW001", "断言错误"),

    /**
     * 服务错误
     */
    SERVER_ERROR("GW500", "%s"),

    /**
     * 权限认证失败
     */
    AUTH_FAIL("GW401", "%s");

    private final String code;
    private final String message;

    GatewayError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
