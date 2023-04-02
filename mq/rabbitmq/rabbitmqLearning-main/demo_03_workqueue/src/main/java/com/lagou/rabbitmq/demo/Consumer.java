package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
//        保险起见，先声明一下，如果RabbitMQ的虚拟主机中有该队列，当然好，如果没有，则创建
//        此处的队列应该和生产者声明的队列属性等一致
        channel.queueDeclare("queue.wq", true, false, false, null);

        channel.basicConsume("queue.wq", new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                System.out.println(new String(message.getBody(), "utf-8"));
            }
        }, new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("consumerTag  :  " + consumerTag);
            }
        });

//        channel.close();
//        connection.close();
    }
}
