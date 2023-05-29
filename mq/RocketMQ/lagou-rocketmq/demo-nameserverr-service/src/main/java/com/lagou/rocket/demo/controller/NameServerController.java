package com.lagou.rocket.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NameServerController {

    @RequestMapping("/rocketmq/nsaddr")
    public String getNameServerAddr() {
        return "node1:9876";
    }

}
