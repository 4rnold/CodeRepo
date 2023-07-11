package mayfly.sys.api.model.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author meilin.huang
 * @date 2022-03-30 15:44
 */
@Data
@Accessors(chain = true)
public class ServiceQueryDTO {

    private String updateTime;
}
