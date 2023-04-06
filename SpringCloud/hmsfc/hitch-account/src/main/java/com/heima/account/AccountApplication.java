package com.heima.account;

import com.heima.commons.initial.annotation.EnableRequestInital;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
//开启fegin支持，clients是指哪个类开启fegin
@EnableFeignClients(basePackages = {"com.heima.account.service"})
@EnableRequestInital
public class AccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);

    }
}
