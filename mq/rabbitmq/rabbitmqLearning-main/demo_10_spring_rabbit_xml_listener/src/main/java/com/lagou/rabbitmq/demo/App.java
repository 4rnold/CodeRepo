package com.lagou.rabbitmq.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-rabbit.xml");
    }
}
