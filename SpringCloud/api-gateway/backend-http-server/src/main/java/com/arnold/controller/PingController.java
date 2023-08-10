package com.arnold.controller;


import com.arnold.gateway.client.core.ApiInvoker;
import com.arnold.gateway.client.core.ApiProperties;
import com.arnold.gateway.client.core.ApiProtocol;
import com.arnold.gateway.client.core.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@Slf4j
@RestController
@ApiService(serviceId = "backend-http-server", protocol = ApiProtocol.HTTP, patternPath = "/http-demo/**")
public class PingController {

    @Autowired
    private ApiProperties apiProperties;

    @ApiInvoker(path = "/http-demo/ping")
    @GetMapping("/http-demo/ping")
    public String ping(Date date) {
        log.warn("/http-demo/ping");
        return "pong";
    }
}
