package com.lagou.rocket.demok.producer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class Consumer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("consuper_grp_17_01");
        consumer.setNamesrvAddr("node1:9876");
        consumer.start();
        final MessageExt messageExt = consumer.viewMessage("tp_demo_17", "C0A8646500002A9F0000000000074933");
        System.out.println(messageExt);
        consumer.shutdown();
    }
}
