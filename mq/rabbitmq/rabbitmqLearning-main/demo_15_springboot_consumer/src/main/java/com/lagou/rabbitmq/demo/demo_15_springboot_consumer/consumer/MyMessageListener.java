package com.lagou.rabbitmq.demo.demo_15_springboot_consumer.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MyMessageListener {


    @RabbitListener(queues = "queue.boot")
    public void getMyMessage(@Payload String message, @Header("hello") String value){
        System.out.println(message);
        System.out.println("hello = "+value);
    }
}
