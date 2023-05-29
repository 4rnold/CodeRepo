package com.lagou.rocket.demo.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class MyConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_grp_10_01");

        consumer.setNamesrvAddr("node1:9876");
        // 设置消息重试次数
        consumer.setMaxReconsumeTimes(5);
        consumer.setConsumeMessageBatchMaxSize(1);

        consumer.subscribe("tp_demo_10", "*");

        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                System.out.println(System.currentTimeMillis() / 1000);
                for (MessageExt msg : msgs) {
                    System.out.println(
                            msg.getTopic() + "\t"
                                    + msg.getQueueId() + "\t"
                                    + msg.getMsgId() + "\t"
                                    + msg.getDelayTimeLevel() + "\t"
                                    + new String(msg.getBody())
                    );
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
    }
}
