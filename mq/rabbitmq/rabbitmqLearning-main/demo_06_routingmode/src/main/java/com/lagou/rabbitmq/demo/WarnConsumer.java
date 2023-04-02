package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class WarnConsumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare("ex.routing", "direct", false, false, null);
        // 此处也可以声明为临时消息队列
        channel.queueDeclare("queue.warn", false, false, false, null);

        channel.queueBind("queue.warn", "ex.routing", "WARN");

        channel.basicConsume("queue.warn", ((consumerTag, message) -> {
            System.out.println(consumerTag);
            System.out.println("WarnConsumer收到的消息：" + new String(message.getBody(), "utf-8"));
        }), consumerTag -> { });

    }
}
