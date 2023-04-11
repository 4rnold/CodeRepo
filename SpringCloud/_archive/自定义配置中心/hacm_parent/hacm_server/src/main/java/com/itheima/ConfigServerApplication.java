package com.itheima;

import com.itheima.init.annotations.EnableConfigServer;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * SpringBoot的入口类
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@SpringBootApplication
@EnableConfigServer
@EnableWebMvc
public class ConfigServerApplication {

    /**
     * 启动方法
     * @param args
     */
    public static void main(String[] args) {
//        SpringApplication springApplication = new SpringApplication(ConfigServerApplication.class);
//        springApplication.setBannerMode(Mode.OFF);
//        springApplication.run(args);
        SpringApplication.run(ConfigServerApplication.class,args);
    }
}
