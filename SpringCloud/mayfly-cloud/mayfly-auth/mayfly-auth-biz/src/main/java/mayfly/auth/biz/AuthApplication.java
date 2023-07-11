package mayfly.auth.biz;

import mayfly.auth.biz.error.AuthError;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.EnableLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author meilin.huang
 * @date 2022-03-19 20:46
 */
@EnableLog
@EnableDiscoveryClient
@EnableFeignClients("mayfly.**.api")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuthApplication {

    public static void main(String[] args) {
        BizAssert.setDefaultErrorCode(AuthError.ASSERT_ERROR.getCode());
        SpringApplication.run(AuthApplication.class);
    }
}
