package com.lagou.rabbitmq.demo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;

public class ConsumerApp {

    public static void main(String[] args) throws UnsupportedEncodingException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring-rabbit.xml");

        final RabbitTemplate template = context.getBean(RabbitTemplate.class);

        final Message message = template.receive("queue.q1");

        // 拉消息模式
        System.out.println(new String(message.getBody(), message.getMessageProperties().getContentEncoding()));
//        System.out.println(new String(message.getBody(), "utf-8"));

        context.close();
    }

}
