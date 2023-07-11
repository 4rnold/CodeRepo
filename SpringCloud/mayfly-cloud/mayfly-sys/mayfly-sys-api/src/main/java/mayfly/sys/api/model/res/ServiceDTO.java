package mayfly.sys.api.model.res;

import lombok.Data;

/**
 * @author meilin.huang
 * @date 2022-03-30 15:12
 */
@Data
public class ServiceDTO {

    private String code;

    private Integer status;

    private Integer isDeleted;
}
