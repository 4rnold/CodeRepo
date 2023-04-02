package com.lagou.rabbitmq.demo.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class MyMeetingListener {

    @RabbitListener(queues = "queue.delayed")
    public void onMessage(Message message, Channel channel) throws IOException {

        System.out.println(new String(message.getBody(), message.getMessageProperties().getContentEncoding()));

        // 消息确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }


}
