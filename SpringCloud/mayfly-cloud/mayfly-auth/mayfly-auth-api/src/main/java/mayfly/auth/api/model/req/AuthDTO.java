package mayfly.auth.api.model.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author meilin.huang
 * @date 2022-03-25 11:14
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AuthDTO {

    /**
     * 请求地址对应的微服务名
     */
    private String service;

    /**
     * 请求方方法
     */
    private String method;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 请求token
     */
    private String token;

    public String getUri() {
        return String.format("%s:%s", method, path);
    }
}
