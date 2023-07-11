package mayfly.auth.biz.permission;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author meilin.huang
 * @date 2022-03-30 15:01
 */
@Data
@Accessors(chain = true)
public class Api {

    /**
     * 请求地址，格式为 method:path
     */
    private String uri;

    /**
     * code
     */
    private String code;

    /**
     * 状态
     */
    private Integer status;
}
