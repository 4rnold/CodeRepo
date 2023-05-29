package com.lagou.rocket.demo.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Set;

public class MyConsumer {

    public static void main(String[] args) throws MQClientException {

        // 消息的拉取
        DefaultMQPullConsumer pullConsumer = new DefaultMQPullConsumer();

        // 消费的模式：集群
        pullConsumer.setMessageModel(MessageModel.CLUSTERING);
        // 消费的模式：广播
        pullConsumer.setMessageModel(MessageModel.BROADCASTING);


        final Set<MessageQueue> messageQueues = pullConsumer.fetchSubscribeMessageQueues("tp_demo_05");

        for (MessageQueue messageQueue : messageQueues) {
            // 指定消息队列，指定标签过滤的表达式，消息偏移量和单次最大拉取的消息个数
            pullConsumer.pull(messageQueue, "TagA||TagB", 0L, 100);
        }


        // 消息的推送
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer();

        pushConsumer.setMessageModel(MessageModel.BROADCASTING);
        pushConsumer.setMessageModel(MessageModel.CLUSTERING);

        // 设置消费者的线程数
        pushConsumer.setConsumeThreadMin(1);
        pushConsumer.setConsumeThreadMax(10);

        // subExpression表示对标签的过滤：
        // TagA||TagB|| TagC    *表示不对消息进行标签过滤
        pushConsumer.subscribe("tp_demo_05", "*");

        // 设置消息批处理的一个批次中消息的最大个数
        pushConsumer.setConsumeMessageBatchMaxSize(10);

        // 在设置完之后调用start方法初始化并运行推送消息的消费者
        pushConsumer.start();
    }


}
