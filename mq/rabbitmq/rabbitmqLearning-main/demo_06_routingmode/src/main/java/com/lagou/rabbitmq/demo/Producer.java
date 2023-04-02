package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;

public class Producer {

    private final static String[] LOG_LEVEL = {
            "ERROR",
            "FATAL",
            "WARN"
    };

    private static Random random = new Random();

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 声明direct类型的交换器，交换器和消息队列的绑定不需要在这里处理
        channel.exchangeDeclare("ex.routing", "direct", false, false, null);

        for (int i = 0; i < 100; i++) {
            String level = LOG_LEVEL[random.nextInt(100) % LOG_LEVEL.length];
            channel.basicPublish("ex.routing", level, null, ("这是【" + level + "】的消息").getBytes());
        }

    }


}
