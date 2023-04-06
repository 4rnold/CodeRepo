package com.heima.stroke.rabbitmq;


import com.heima.stroke.configuration.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 发送延时订单MQ
     *
     * @param mqMessage
     */
    public void sendOver(String mqMessage) {
        //发送消息
        rabbitTemplate.convertAndSend(RabbitConfig.STROKE_OVER_QUEUE_EXCHANGE, RabbitConfig.STROKE_OVER_KEY, mqMessage);
    }

    /**
     * 发送位置信息
     *
     * @param mqMessage
     */
    public void sendLocation(String mqMessage) {
        //发送消息
        rabbitTemplate.convertAndSend(RabbitConfig.STROKE_LOCATION_QUEUE_EXCHANGE, RabbitConfig.STROKE_LOCATION_KEY, mqMessage);
    }
}
