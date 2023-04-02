package com.lagou.rabbitmq.demo.demo_15_springboot_consumer.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue(){
        return QueueBuilder.nonDurable("queue.boot").build();
    }


}
