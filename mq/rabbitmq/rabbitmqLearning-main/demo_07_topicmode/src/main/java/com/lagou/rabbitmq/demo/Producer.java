package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;

public class Producer {

    private static final String[] LOG_LEVEL = {"info", "error", "warn"};
    private static final String[] LOG_AREA = {"beijing", "shanghai", "shenzhen"};
    private static final String[] LOG_BIZ = {"edu-online", "biz-online", "emp-online"};

    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws Exception {

        final ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare("ex.topic", "topic", true, false, null);

        String area, level, biz;

        String routingKey, message;
        for (int i = 0; i < 100; i++) {

            area = LOG_AREA[RANDOM.nextInt(LOG_AREA.length)];
            level = LOG_LEVEL[RANDOM.nextInt(LOG_LEVEL.length)];
            biz = LOG_BIZ[RANDOM.nextInt(LOG_BIZ.length)];

            // routingKey中包含了三个维度
            routingKey = area + "." + biz + "." + level;
            message = "LOG: [" + level + "] :这是 [" + area + "] 地区 [" + biz + "] 服务器发来的消息，MSG_SEQ = " + i;

            channel.basicPublish("ex.topic", routingKey, null, message.getBytes("utf-8"));
        }

        channel.close();
        connection.close();
    }
}
