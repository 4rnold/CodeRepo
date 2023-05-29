package com.lagou.rocket.demo.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class MyProducer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("producer_grp_08_01");

        producer.setNamesrvAddr("node1:9876");

        producer.start();

        Message message = null;

        for (int i = 0; i < 10; i++) {

            message = new Message("tp_demo_08", ("hello lagou - " + i).getBytes());

            final SendResult result = producer.send(message);
            System.out.println(result.getSendStatus());

        }

        producer.shutdown();

    }
}
