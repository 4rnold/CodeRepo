package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class BeijingConsumer {
    public static void main(String[] args) throws Exception {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 临时队列，返回值是服务器为该队列生成的名称
        final String queue = channel.queueDeclare().getQueue();
        channel.exchangeDeclare("ex.topic", "topic", true, false, null);
//       beijing.biz-online.error
//        只要routingKey是以beijing开头的，后面不管几个点分单词，都可以接收
        channel.queueBind(queue, "ex.topic", "beijing.#");

        channel.basicConsume(queue, (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "utf-8"));
        }, consumerTag -> {});

    }
}
