package mayfly.sys.api.model.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author meilin.huang
 * @date 2022-03-19 19:59
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AccountQueryDTO {

    private String username;
}
