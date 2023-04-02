package com.lagou.rabbitmq.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("queue.haproxy",
                true,
                false,
                false,
                null);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange("ex.haproxy",
                true,
                false,
                null);
    }

    @Bean
    public Binding binding() {
        return new Binding("queue.haproxy",
                Binding.DestinationType.QUEUE,
                "ex.haproxy",
                "key.haproxy", null);
    }

    @Bean
    @Autowired
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

}
