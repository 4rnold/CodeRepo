package com.lagou.rocket.demo.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class MyProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("producer_grp_14_01");

        producer.setNamesrvAddr("node1:9876");

        producer.start();

        Message message = new Message("tp_demo_14", "hello lagou".getBytes());
        // tag用于标记一类消息
        message.setTags("tag1");

        // keys用于建立索引的时候，hash取模将消息的索引放到SlotTable的一个Slot链表中
        message.setKeys("oid_2020-12-30_567890765");

        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable e) {

            }
        });

        producer.shutdown();
    }
}
