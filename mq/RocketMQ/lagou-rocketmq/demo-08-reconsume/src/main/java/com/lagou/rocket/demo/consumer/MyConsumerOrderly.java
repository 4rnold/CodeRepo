package com.lagou.rocket.demo.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class MyConsumerOrderly {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_08_01");
        consumer.setNamesrvAddr("node1:9876");

        // 一次获取一条消息
        consumer.setConsumeMessageBatchMaxSize(1);

        // 订阅主题
        consumer.subscribe("tp_demo_08", "*");

        consumer.setMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

                for (MessageExt msg : msgs) {
                    System.out.println(msg.getMsgId() + "\t" + msg.getQueueId() + "\t" + new String(msg.getBody()));
                }

//                return ConsumeOrderlyStatus.SUCCESS;  // 确认消息
//                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT; // 引发重试
                return null; // 引发重试
            }
        });


        consumer.start();
    }
}
