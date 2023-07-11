package mayfly.auth.biz.contoller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @date 2022-03-21 10:58
 */
@Data
public class AccountVO {

    private Long id;

    private String username;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;
}
