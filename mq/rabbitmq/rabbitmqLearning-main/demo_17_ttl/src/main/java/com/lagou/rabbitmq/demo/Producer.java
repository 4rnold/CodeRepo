package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        try (final Connection connection = factory.newConnection();
             final Channel channel = connection.createChannel()) {

            Map<String, Object> arguments = new HashMap<>();
//            消息队列中消息过期时间，30s
            arguments.put("x-message-ttl", 10 * 1000);
//            如果消息队列没有消费者，则10s后消息过期，消息队列也删除
//            arguments.put("x-expires", 10 * 1000);
            arguments.put("x-expires", 60 * 1000);

            channel.queueDeclare("queue.ttl.waiting",
                    true,
                    false,
                    false,
                    arguments);

            channel.exchangeDeclare("ex.ttl.waiting",
                    "direct",
                    true,
                    false,
                    null);

            channel.queueBind("queue.ttl.waiting", "ex.ttl.waiting", "key.ttl.waiting");

            final AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .contentEncoding("utf-8")
                    .deliveryMode(2)   // 持久化的消息
                    .build();

            channel.basicPublish("ex.ttl.waiting",
                    "key.ttl.waiting",
                    null,
                    "等待的订单号".getBytes("utf-8"));


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
