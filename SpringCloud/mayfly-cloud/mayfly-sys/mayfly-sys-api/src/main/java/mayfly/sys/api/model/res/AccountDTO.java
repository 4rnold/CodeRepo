package mayfly.sys.api.model.res;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @date 2022-03-19 20:00
 */
@Data
public class AccountDTO {

    private Long id;

    private String username;

    private String password;

    /**
     *
     */
    private Integer status;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;
}
