package mayfly.sys.api.model.res;

import lombok.Data;

/**
 * @author meilin.huang
 * @date 2022-03-30 15:10
 */
@Data
public class ApiDTO {

    private String uri;

    /**
     * 请求方法
     *
     * @see mayfly.sys.api.enums.HttpMethodEnum
     */
    private String method;

    private String code;

    private Integer status;

    private Integer isDeleted;
}
