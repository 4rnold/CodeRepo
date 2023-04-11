package com.itheima.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制器
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@RestController
@RequestMapping("/config")
public class ContextRefreshController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 用于接收业务微服务的请求，处理刷新配置
     * 它本身不是真正请求黑马应用配置管理平台，而是往队列中写入消息
     * 需要在引入client的业务微服务中配置mq的host
     */
    @RequestMapping(value = "/refresh",method = RequestMethod.POST)
    public void refresh(){
        rabbitTemplate.convertAndSend("heimaconfig","refresh");
    }
}


