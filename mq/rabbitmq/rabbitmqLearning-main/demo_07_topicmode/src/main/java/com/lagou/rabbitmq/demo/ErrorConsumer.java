package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ErrorConsumer {
    public static void main(String[] args) throws Exception {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 临时队列，返回值是服务器为该队列生成的名称
        final String queue = channel.queueDeclare().getQueue();
        channel.exchangeDeclare("ex.topic", "topic", true, false, null);
//       beijing.biz-online.error
//        不管前面有几个点分单词，只要最后一个点分单词是error就可以路由消息
        channel.queueBind(queue, "ex.topic", "#.error");

        channel.basicConsume(queue, (consumerTag, message) -> {
            System.out.println(new String(message.getBody(), "utf-8"));
        }, consumerTag -> {});

    }
}
