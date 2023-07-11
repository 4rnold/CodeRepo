package mayfly.sys.biz;

import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.EnableLog;
import mayfly.sys.biz.common.error.SysError;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @date 2018/6/7 下午5:21
 */
@EnableLog
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = "mayfly.**.mapper")
public class SysApplication {
    public static void main(String[] args) {
        BizAssert.setDefaultErrorCode(SysError.ASSERT_ERROR.getCode());
        SpringApplication.run(SysApplication.class);
    }
}
