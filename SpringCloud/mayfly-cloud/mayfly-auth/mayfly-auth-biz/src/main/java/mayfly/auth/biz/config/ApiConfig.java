package mayfly.auth.biz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author meilin.huang
 * @date 2022-04-08 10:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "api")
public class ApiConfig {

    /**
     * 是否校验api权限
     */
    private boolean check;
}
