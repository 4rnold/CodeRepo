package com.itheima.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args)throws Exception {
        //启动方法
        SpringApplication.run(OrderApplication.class);

        //1.创建事务消息的消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("txmessage_trans-client-group");
        //2.设置nameserver的地址
        consumer.setNamesrvAddr("127.0.0.1:9876");
        //3.设置单次消费的消息数量
        consumer.setConsumeMessageBatchMaxSize(5);
        //4.设置消息消费顺序
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //5.设置监听消息的信息topic和tags
        consumer.subscribe("txmessage_topic","txmessage_tags");
        //6.编写监听消息的监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    for(MessageExt  messageExt : msgs){
                        //取出消息内容
                        String topic = messageExt.getTopic();
                        String tags = messageExt.getTags();
                        String keys = messageExt.getKeys();
                        String msg = new String(messageExt.getBody(),"UTF-8");
                        String transactionId = messageExt.getTransactionId();
                        System.out.println("获取到的消息：topic="+topic+",tags="+tags+",keys="+keys+",transactionId="+transactionId+",message="+msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        System.out.println("启动消费者");
        consumer.start();
    }
}
