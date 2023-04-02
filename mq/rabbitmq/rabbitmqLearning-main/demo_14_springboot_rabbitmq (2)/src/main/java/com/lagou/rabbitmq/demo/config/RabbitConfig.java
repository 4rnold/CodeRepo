package com.lagou.rabbitmq.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("queue.boot", false, false, false, null);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange("ex.boot", false, false, null);
    }


    @Bean
    public Binding binding() {
        return new Binding("queue.boot",
                Binding.DestinationType.QUEUE,
                "ex.boot",
                "key.boot",
                null);
    }

}
