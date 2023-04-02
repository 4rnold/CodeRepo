package com.lagou.rabbitmq.demo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.UnsupportedEncodingException;

public class ConsumerApp {
    public static void main(String[] args) throws UnsupportedEncodingException {

        // 从指定类加载配置信息
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(RabbitConfig.class);
        // 获取RabbitTemplate对象
        final RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);
        // 接收消息
        final Message message = rabbitTemplate.receive("queue.anno");
        // 打印消息
        System.out.println(new String(message.getBody(), message.getMessageProperties().getContentEncoding()));

        // 关闭spring的上下文
        context.close();

    }
}
