package mayfly.sys.biz.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Getter
@Setter
@ToString
@Table("t_account")
public class AccountDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = -5172351936029693334L;

    private String username;

    private String password;

    private Integer status;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;
}
