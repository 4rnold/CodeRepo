package com.itheima;

import com.itheima.annotations.EnableConfigClient;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@SpringBootApplication
@EnableConfigClient
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class,args);
    }
}
