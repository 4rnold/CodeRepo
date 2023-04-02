package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class HelloConsumeConsumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        // 确保MQ中有该队列，如果没有则创建
        channel.queueDeclare("queue.biz", false, false, true, null);


        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {

            }

        };
        // 监听消息，一旦有消息推送过来，就调用第一个lambda表达式
        channel.basicConsume("queue.biz", (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        }, (consumerTag) -> {});

//        channel.close();
//        connection.close();
    }
}
