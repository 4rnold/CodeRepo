package com.lagou.rocket.demo.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class MyProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("producer_grp_16_02");
        // 不使用代码指定，在启动参数中指定
//        producer.setNamesrvAddr("node1:9876");

        producer.start();

        Message message = new Message("tp_demo_17", "hello lagou".getBytes());
        SendResult sendResult = producer.send(message);

        System.out.println(sendResult.getSendStatus());
        System.out.println(sendResult.getMsgId());
        System.out.println(sendResult.getQueueOffset());

        producer.shutdown();

    }
}