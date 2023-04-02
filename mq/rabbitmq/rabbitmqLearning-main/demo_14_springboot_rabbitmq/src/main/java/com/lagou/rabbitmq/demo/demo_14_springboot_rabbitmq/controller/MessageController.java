package com.lagou.rabbitmq.demo.demo_14_springboot_rabbitmq.controller;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class MessageController {

    @Autowired
    private AmqpTemplate rabbitTemplate;


    @RequestMapping("rabbit/{message}")
    public String receive(@PathVariable String message) throws UnsupportedEncodingException {

        MessageProperties messageProperties = MessagePropertiesBuilder.newInstance().setContentEncoding("utf-8")
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setHeader("hello", "world")
                .build();

        Message msg = MessageBuilder.withBody(message.getBytes("utf-8")).andProperties(messageProperties).build();

        rabbitTemplate.send("ex.boot", "key.boot", msg);

        return "OK";
    }

}
