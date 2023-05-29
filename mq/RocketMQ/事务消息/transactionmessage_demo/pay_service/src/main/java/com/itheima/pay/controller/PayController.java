package com.itheima.pay.controller;

import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PayController {

    @Resource
    private TransactionListener transactionListener;


    @RequestMapping(value = "/pay/updateOrder", method = RequestMethod.POST)
    public String payOrder(@RequestParam("payid") String id, @RequestParam("ispay") int ispay) {
        try {
            //1.创建消息的生产者
            TransactionMQProducer transactionMQProducer = new TransactionMQProducer("txmessage_trans-client-group");
            //2.指定服务器地址nameserver
            transactionMQProducer.setNamesrvAddr("127.0.0.1:9876");
            //3.设置消息回查的监听器
            transactionMQProducer.setTransactionListener(transactionListener);
            //4.创建消息对象
            Message message = new Message("txmessage_topic", "txmessage_tags", "txmessage_keys", "txmessage_事务消息".getBytes(RemotingHelper.DEFAULT_CHARSET));
            //启动生产者
            transactionMQProducer.start();
            //5.准备数据
            Map<String,Object> payArgs = new HashMap<>();
            payArgs.put("id",id);
            payArgs.put("ispay",ispay);
            //6.发送消息
            transactionMQProducer.sendMessageInTransaction(message,payArgs);
            //7.释放资源（关闭消息发送者）
            transactionMQProducer.shutdown();
        }catch (Exception e){
            e.printStackTrace();
            return "发送消息给mq失败";
        }
        //如果没有问题,
        return "发送消息给mq成功";
    }

}
