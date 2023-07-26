package com.arnold.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PingController {

    @GetMapping("/http-demo/ping")
    public String ping() {
        log.warn("/http-demo/ping");
        return "pong";
    }
}
