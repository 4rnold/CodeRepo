package mayfly.sys.api.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 更新账号登录请求体
 *
 * @author meilin.huang
 * @date 2022-04-12 11:05
 */
@Accessors(chain = true)
@Data
public class UpdateAccountLoginReqDTO {

    private Long accountId;

    private String loginIp;

    private LocalDateTime loginTime;
}
