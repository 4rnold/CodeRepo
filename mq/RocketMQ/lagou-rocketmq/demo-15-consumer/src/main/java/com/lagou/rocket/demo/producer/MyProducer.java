package com.lagou.rocket.demo.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class MyProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("producer_grp_15_01");

//        producer.setNamesrvAddr("node1:9876");

        producer.start();

        Message message = new Message("tp_demo_15", "hello lagou".getBytes());

        final SendResult result = producer.send(message);

        System.out.println(result.getSendStatus());
        System.out.println(result.getMsgId());
        System.out.println(result.getOffsetMsgId());

        producer.shutdown();
    }
}
