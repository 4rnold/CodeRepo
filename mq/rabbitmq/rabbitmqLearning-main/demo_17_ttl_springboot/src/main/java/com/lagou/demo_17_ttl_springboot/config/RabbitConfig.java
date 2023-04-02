package com.lagou.demo_17_ttl_springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;



@Configuration
public class RabbitConfig {

    @Bean
    public Queue queueTTLWaiting() {
        Map<String, Object> props = new HashMap<>();
// 对于该队列中的消息，设置都等待10s
        props.put("x-message-ttl", 10000);
        Queue queue = new Queue("q.pay.ttl-waiting", false, false,
                false, props);
        return queue;
    }
    @Bean
    public Queue queueWaiting() {
        Queue queue = new Queue("q.pay.waiting", false, false,
                false);
        return queue;
    }

    @Bean
    public Exchange exchangeTTLWaiting() {
        DirectExchange exchange = new DirectExchange("ex.pay.ttl-waiting", false, false);
        return exchange;
    }
    /**
     * 该交换器使用的时候，需要给每个消息设置有效期
     * @return
     */
    @Bean
    public Exchange exchangeWaiting() {
        DirectExchange exchange = new
                DirectExchange("ex.pay.waiting", false, false);
        return exchange;
    }
    @Bean
    public Binding bindingTTLWaiting() {
        return
                BindingBuilder.bind(queueTTLWaiting()).to(exchangeTTLWaiting()).with("pay.ttl-waiting").noargs();
    }
    @Bean
    public Binding bindingWaiting() {
        return BindingBuilder.bind(queueWaiting()).to(exchangeWaiting()).with("pay.waiting").noargs();
    }
}
