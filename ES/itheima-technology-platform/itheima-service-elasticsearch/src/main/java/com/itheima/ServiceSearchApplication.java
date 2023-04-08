package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Class: ServiceSearchApplication
 * @Package com.itheima.controller
 * @Description:
 * @Company: http://www.itheima.com/
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RefreshScope

public class ServiceSearchApplication {
    /*
     * @Description全文检索入口
     * @Param  [args]
     * @Return      void
     * @Exception
     *
     */
    public static void main(String[] args) {
        SpringApplication.run(ServiceSearchApplication.class, args);
    }

}
