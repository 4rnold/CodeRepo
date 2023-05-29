package com.lagou.rocket.demo.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyConsumer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("mygrp_consume");
        consumer.setNamesrvAddr("node1:9876");
        consumer.start();

        Map<MessageQueue, Long> offsetMap = new HashMap<>();

        Set<MessageQueue> messageQueues = consumer.fetchSubscribeMessageQueues("tp_admin_01");
        while (true) {
            for (MessageQueue messageQueue : messageQueues) {
                Long aLong = offsetMap.get(messageQueue);
                if (aLong == null) {
                    offsetMap.put(messageQueue, 0L);
                }
                PullResult pull = consumer.pull(messageQueue, "*", offsetMap.get(messageQueue), 1);
                List<MessageExt> msgFoundList = pull.getMsgFoundList();
                if (msgFoundList != null) {
                    System.out.println(pull.getMsgFoundList().size());
                    offsetMap.put(messageQueue, pull.getNextBeginOffset());
                }
                Thread.sleep(1000);
            }
        }
    }
}