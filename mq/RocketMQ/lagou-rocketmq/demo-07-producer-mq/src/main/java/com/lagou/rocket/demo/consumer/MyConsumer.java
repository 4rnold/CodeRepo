package com.lagou.rocket.demo.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class MyConsumer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("consumer_grp_06_01");
        consumer.setNamesrvAddr("node1:9876");

        consumer.start();

        final PullResult pullResult = consumer.pull(new MessageQueue(
                        "tp_demo_06",
                        "node1",
                        4
                ),
                "*",
                0L,
                10);

        System.out.println(pullResult);

        pullResult.getMsgFoundList().forEach(messageExt -> {
            System.out.println(messageExt);
        });

        consumer.shutdown();

    }
}
