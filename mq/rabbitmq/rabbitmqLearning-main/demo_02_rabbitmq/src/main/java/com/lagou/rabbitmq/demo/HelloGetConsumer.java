package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class HelloGetConsumer {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 指定协议： amqp://
        // 指定用户名  root
        // 指定密码   123456
        // 指定host   node1
        // 指定端口号  5672
        // 指定虚拟主机  %2f (%2f是/的转义字符)
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        final Connection connection = factory.newConnection();
        System.out.println(connection.getClass());

        final Channel channel = connection.createChannel();

        // 拉消息模式
        // 指定从哪个消费者消费消息
        // 指定是否自动确认消息  true表示自动确认
        final GetResponse getResponse = channel.basicGet("queue.biz", true);
        // 获取消息体  hello world 1
        final byte[] body = getResponse.getBody();
        System.out.println(new String(body));

//        System.out.println(getResponse.getEnvelope());
//        final AMQP.BasicProperties props = getResponse.getProps();

        channel.close();
        connection.close();

    }
}
