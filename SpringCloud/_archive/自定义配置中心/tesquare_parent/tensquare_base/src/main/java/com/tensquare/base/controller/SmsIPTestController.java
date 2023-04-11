package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@RestController
@RequestMapping("/test")
@CrossOrigin
@RefreshScope //自定义RefreshScope
public class SmsIPTestController {

    @Value("${sms.ip}")
    private String ip;

    public SmsIPTestController(){
        System.out.println("SmsIPTestController 实例化了");
    }

    @PostConstruct
    public void init(){
        System.out.println("init method print ip is "+ip);
    }


    @Autowired
    private ConfigurableApplicationContext context;

    @RequestMapping(method = RequestMethod.GET,value = "/smsip")
    public Result testSmsIP(){
        return new Result(true, StatusCode.OK,"获取成功",ip);
    }



}
