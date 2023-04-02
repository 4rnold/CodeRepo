package com.lagou.rabbitmq.demo.controller;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class BizController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/biz/{hello}")
    public String doBiz(@PathVariable String hello) throws UnsupportedEncodingException {

        final MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setContentType("text/plain")
                .setContentEncoding("utf-8")
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setHeader("mykey", "myvalue")
                .build();

        final Message message = MessageBuilder
                .withBody(hello.getBytes("utf-8"))
                .andProperties(properties)
                .build();

        amqpTemplate.send("ex.haproxy", "key.haproxy", message);

        return "ok";
    }

}
