package mayfly.sys.api.enums;

import mayfly.core.util.enums.ValueEnum;

/**
 * @author meilin.huang
 * @date 2022-03-31 16:06
 */
public enum HttpMethodEnum implements ValueEnum<String> {
    /**
     * GET
     */
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String value;

    HttpMethodEnum(String method) {
        this.value = method;
    }

    @Override
    public String getValue() {
        return value;
    }
}
