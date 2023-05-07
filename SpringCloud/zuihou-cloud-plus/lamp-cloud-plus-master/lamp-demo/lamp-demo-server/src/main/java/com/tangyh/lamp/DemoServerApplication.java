package com.tangyh.lamp;

import com.alibaba.cloud.seata.feign.SeataFeignClientAutoConfiguration;
import com.tangyh.basic.security.annotation.EnableLoginArgResolver;
import com.tangyh.basic.validator.annotation.EnableFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.tangyh.lamp.common.constant.BizConstant.BUSINESS_PACKAGE;
import static com.tangyh.lamp.common.constant.BizConstant.UTIL_PACKAGE;

/**
 * @author zuihou
 */
@SpringBootApplication(exclude = {SeataFeignClientAutoConfiguration.class})
@EnableDiscoveryClient
@EnableHystrix
@ComponentScan({
        UTIL_PACKAGE, BUSINESS_PACKAGE
})
@EnableFeignClients(value = {
        UTIL_PACKAGE, BUSINESS_PACKAGE
})
@Slf4j
@EnableLoginArgResolver
@EnableFormValidator
public class DemoServerApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(DemoServerApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "应用 '{}' 运行成功! 访问连接:\n\t" +
                        "Swagger文档: \t\thttp://{}:{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));

    }
}
