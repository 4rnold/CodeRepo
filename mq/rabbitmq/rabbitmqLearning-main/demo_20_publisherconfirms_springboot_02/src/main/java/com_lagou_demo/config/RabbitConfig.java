package com_lagou_demo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Bean
    public Queue queue() {
        Queue queue = new Queue("q.biz", false, false, false, null);
        return queue;
    }

    @Bean
    public Exchange exchange() {
        Exchange exchange = new DirectExchange("ex.biz", false, false, null);
        return exchange;
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("biz").noargs();
    }

}
