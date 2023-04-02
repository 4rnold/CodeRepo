package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        // 声明消息队列
        channel.queueDeclare("queue.wq", true, false, false, null);
        //声明一个交换器
        channel.exchangeDeclare("ex.wq", BuiltinExchangeType.DIRECT, true, false, null);
        // 将交换器绑定到消息队列，同时指定绑定键（binding-key）
        channel.queueBind("queue.wq", "ex.wq", "key.wq");
//        channel.exchangeBind("ex.wq", "queue.wq", "key.wq");
        for (int i = 0; i < 15; i++) {
            channel.basicPublish("ex.wq", "key.wq", null, ("工作队列：" + i).getBytes("utf-8"));
        }

        // 关闭通道
        channel.close();
        // 关闭连接
        connection.close();
    }
}
