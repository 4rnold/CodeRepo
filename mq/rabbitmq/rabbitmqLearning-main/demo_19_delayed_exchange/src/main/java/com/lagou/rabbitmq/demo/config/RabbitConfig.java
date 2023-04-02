package com.lagou.rabbitmq.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
@ComponentScan("com.lagou.rabbitmq.demo")
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("queue.delayed", true, false, false, null);
    }

    @Bean
    public Exchange exchange() {

        Map<String, Object> arguments = new HashMap<>();
        // 使用x-delayed-type指定交换器的类型(交换机真正类型)
        arguments.put("x-delayed-type", ExchangeTypes.DIRECT);
        // 使用x-delayed-message表示使用delayed exchange插件处理消息
        return new CustomExchange("ex.delayed", "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("key.delayed").noargs();
    }

    @Bean
    @Autowired
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    @Autowired
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        return new RabbitTemplate(factory);
    }

    @Bean
    @Autowired
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory
                = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        return factory;
    }

}
