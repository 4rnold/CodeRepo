package com.lagou.rabbitmq.demo.controller;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class DelayedController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/meeting/{second}")
    public String bookMeeting(@PathVariable Integer second) throws UnsupportedEncodingException {

        final MessageProperties messageProperties = MessagePropertiesBuilder.newInstance()
                // 设置消息的过期时间
                .setHeader("x-delay", (second - 10) * 1000)
                .setContentEncoding("utf-8")
                .build();

        final Message message = MessageBuilder
                .withBody("还有10s开始开会了".getBytes("utf-8"))
                .andProperties(messageProperties)
                .build();

        amqpTemplate.send("ex.delayed", "key.delayed", message);

        return "会议定好了";
    }

}
