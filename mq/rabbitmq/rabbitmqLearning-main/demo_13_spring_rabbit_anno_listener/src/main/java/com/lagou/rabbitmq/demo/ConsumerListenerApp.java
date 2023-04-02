package com.lagou.rabbitmq.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConsumerListenerApp {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(RabbitConfig.class);
    }
}
