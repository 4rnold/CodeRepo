package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class OneConsumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

//        声明临时队列，队列的名字由RabbitMQ自动生成
        final String queueName = channel.queueDeclare().getQueue();
        System.out.println("生成的临时队列的名字为：" + queueName);

        channel.exchangeDeclare("ex.myfan",
                BuiltinExchangeType.FANOUT,
                true,
                false,
                null);

        // fanout类型的交换器绑定不需要routingkey
        channel.queueBind(queueName, "ex.myfan", "");

        channel.basicConsume(queueName, (consumerTag, message) -> {
            System.out.println("One   " + new String(message.getBody(), "utf-8"));
        }, consumerTag -> {});

    }
}
