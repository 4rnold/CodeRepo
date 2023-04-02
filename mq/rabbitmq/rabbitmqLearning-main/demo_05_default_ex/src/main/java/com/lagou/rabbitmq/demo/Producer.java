package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");
//        factory.setHost("node1");
//        factory.setVirtualHost("/");
//        factory.setUsername("root");
//        factory.setPassword("123456");
//        factory.setPort(5672);

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare("queue.default.ex",  // 队列的名字
                false,  // 能否活到MQ重启
                false,   // 是否只能你自己的连接来使用
                false,  // 是否自动删除
                null); // 有没有属性

        // 在发送消息的时候没有指定交换器的名字，此时使用的是默认的交换器，默认交换器就没有名字
        // 路由键就是目的地消息队列的名字
        channel.basicPublish("", "queue.default.ex", null, "hello lagou".getBytes());

        channel.close();
        connection.close();

    }
}
