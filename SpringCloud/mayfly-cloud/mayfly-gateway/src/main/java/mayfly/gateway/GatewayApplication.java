package mayfly.gateway;

import mayfly.core.exception.BizAssert;
import mayfly.gateway.error.GatewayError;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author meilin.huang
 * @date 2022-03-16 15:29
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"mayfly.**.api"})
public class GatewayApplication {

    public static void main(String[] args) {
        // 设置本系统业务断言失败的默认错误码
        BizAssert.setDefaultErrorCode(GatewayError.ASSERT_ERROR.getCode());
        SpringApplication.run(GatewayApplication.class, args);
    }
}
