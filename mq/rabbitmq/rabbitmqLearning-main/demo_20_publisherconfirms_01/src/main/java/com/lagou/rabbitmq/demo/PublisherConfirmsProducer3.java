package com.lagou.rabbitmq.demo;

import com.rabbitmq.client.*;

import javax.management.loading.MLet;
import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class PublisherConfirmsProducer3 {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:admin@handsomejay.work:5672/%2f");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 向RabbitMQ服务器发送AMQP命令，将当前通道标记为发送方确认通道
        final AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();

        channel.queueDeclare("queue.pc", true, false, false, null);
        channel.exchangeDeclare("ex.pc", "direct", true, false, null);
        channel.queueBind("queue.pc", "ex.pc", "key.pc");

//        ConfirmCallback clearOutstandingConfirms = new ConfirmCallback() {
//            @Override
//            public void handle(long deliveryTag, boolean multiple) throws IOException {
//                if (multiple) {
//                    System.out.println("编号小于等于 " + deliveryTag + " 的消息都已经被确认了");
//                } else {
//                    System.out.println("编号为：" + deliveryTag + " 的消息被确认");
//                }
//            }
//        };

        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        ConfirmCallback clearOutstandingConfirms = (deliveryTag, multiple) -> {
            if (multiple) {
                System.out.println("编号小于等于 " + deliveryTag + " 的消息都已经被确认了");
                final ConcurrentNavigableMap<Long, String> headMap
                        = outstandingConfirms.headMap(deliveryTag, true);
                // 清空outstandingConfirms中已经被确认的消息信息
                headMap.clear();

            } else {
                // 移除已经被确认的消息
                outstandingConfirms.remove(deliveryTag);
                System.out.println("编号为：" + deliveryTag + " 的消息被确认");
            }
        };

        // 设置channel的监听器，处理确认的消息和不确认的消息
        channel.addConfirmListener(clearOutstandingConfirms, (deliveryTag, multiple) -> {
            if (multiple) {
                // 将没有确认的消息记录到一个集合中
                // 此处省略实现
                System.out.println("消息编号小于等于：" +  deliveryTag + " 的消息 不确认");
            } else {
                System.out.println("编号为：" + deliveryTag + " 的消息不确认");
            }
        });

        String message = "hello-";
        for (int i = 0; i < 1000; i++) {
            // 获取下一条即将发送的消息的消息ID
            final long nextPublishSeqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("ex.pc", "key.pc", null, (message + i).getBytes());
            System.out.println("编号为：" + nextPublishSeqNo + " 的消息已经发送成功，尚未确认");
            outstandingConfirms.put(nextPublishSeqNo, (message + i));
        }

        // 等待消息被确认
        Thread.sleep(10000);

        channel.close();
        connection.close();
    }
}
