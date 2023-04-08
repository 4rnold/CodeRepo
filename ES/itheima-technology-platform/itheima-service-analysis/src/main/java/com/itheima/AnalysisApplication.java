package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Class: AnalysisApplication
 * @Package com
 * @Description: 分析入口
 * @Company: http://www.itheima.com/
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RefreshScope
public class AnalysisApplication {
    /*
     * @Description全文检索入口
     * @Param  [args]
     * @Return      void
     * @Exception
     *
     */
    public static void main(String[] args) {
        SpringApplication.run(AnalysisApplication.class, args);
    }


}
