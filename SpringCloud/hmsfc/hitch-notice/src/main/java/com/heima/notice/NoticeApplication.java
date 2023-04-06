package com.heima.notice;

import com.heima.commons.initial.annotation.EnableRequestInital;
import com.heima.commons.utils.SpringUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//开启fegin支持，clients是指哪个类开启fegin
@EnableFeignClients(basePackages = {"com.heima.notice.service"})
@EnableRequestInital
@Import(SpringUtil.class)
public class NoticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeApplication.class, args);
    }

}
