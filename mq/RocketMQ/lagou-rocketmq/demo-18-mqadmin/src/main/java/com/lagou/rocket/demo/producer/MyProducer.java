package com.lagou.rocket.demo.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class MyProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("mygrp");
        producer.setNamesrvAddr("node1:9876");
        producer.start();

        Message message = null;
        for (int i = 0; i < 1000; i++) {
            message = new Message("tp_admin_01", ("hello lagou - " + i).getBytes());
            producer.send(message);
            Thread.sleep(1000);
        }

        producer.shutdown();
    }
}