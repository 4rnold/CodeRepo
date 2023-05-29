package com.lagou.rocket.demo.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public class TxProducer {
    public static void main(String[] args) throws MQClientException {
        TransactionListener listener = new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // 当发送事务消息prepare(half)成功后，调用该方法执行本地事务
                System.out.println("执行本地事务，参数为：" + arg);

                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
//                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // 如果没有收到生产者发送的Half Message的响应，broker发送请求到生产者回查生产者本地事务的状态
                // 该方法用于获取本地事务执行的状态。
                System.out.println("检查本地事务的状态：" + msg);
                return LocalTransactionState.COMMIT_MESSAGE;
//                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        };

        TransactionMQProducer producer = new TransactionMQProducer("tx_producer_grp_12");
        // 设置事务的监听器
        producer.setTransactionListener(listener);
        producer.setNamesrvAddr("node1:9876");

        producer.start();

        Message message = null;

        message = new Message("tp_demo_12", "hello lagou - tx - 02".getBytes());
        // 发送事务消息
        producer.sendMessageInTransaction(message, "{\"name\":\"zhangsan\"}");

    }
}