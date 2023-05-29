package com.itheima.pay.listener;

import com.itheima.pay.domain.Pay;
import com.itheima.pay.service.PayService;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Component
public class PayTransactionListener implements TransactionListener {

    /**
     * 对于MQ来说，它就3个状态  1正在执行  2执行成功  3执行失败
     * 只有在执行成功时（事务状态是Commit时）才发送消息
     * 如果执行失败了（事务状态是rollback），就不再发送消息，同时会把half（收取支付凭证）消息删除。
     */

    //用于存储事务的唯一标识和事务的执行状态
    private ConcurrentHashMap<String,Integer> transMap = new ConcurrentHashMap<>();


    @Autowired
    private PayService payService;

    /**
     * 获取本地事务执行的状态（执行本地事务，返回执行结果）
     * @param message
     * @param o
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        //1.获取当前事务消息的唯一标识
        String transactionId = message.getTransactionId();
        //2.设置事务的执行状态是正在执行
        transMap.put(transactionId,1);
        //3.获取参数
        Map<String,Object> payArgs = (Map<String,Object>)o;
        String id = (String)payArgs.get("id");
        Integer ispay = (Integer)payArgs.get("ispay");

        try {
            //4.执行更新支付状态
            System.out.println("支付状态更新开始");
            Pay pay = new Pay();
            pay.setId(id);
            pay.setIspay(ispay);
            payService.update(pay);
            System.out.println("支付状态更新成功");
//            int i=1/0;
            //5.记录执行状态
            transMap.put(transactionId,2);
        }catch (Exception e){
            e.printStackTrace();
            //设置事务状态
            transMap.put(transactionId,3);
            //返回本地事务状态为Rollback
            System.out.println("本地事务执行的结果是"+LocalTransactionState.ROLLBACK_MESSAGE);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        System.out.println("本地事务执行的结果是"+LocalTransactionState.COMMIT_MESSAGE);
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    /**
     * 回查本地事务执行状态
     * @param messageExt
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        //1.获取当前事务消息的唯一标识
        String transactionId = messageExt.getTransactionId();
        //2.根据事务id，从map中获取事务执行的状态
        Integer state = transMap.get(transactionId);
        LocalTransactionState localTransactionState = null;
        //3.判断事务状态执行的结果
        switch (state){
            case 2:
                //执行成功
                System.out.println("本地事务执行的结果是"+LocalTransactionState.COMMIT_MESSAGE);
                localTransactionState = LocalTransactionState.COMMIT_MESSAGE;
                break;
            case 3:
                //执行失败
                System.out.println("本地事务执行的结果是"+LocalTransactionState.ROLLBACK_MESSAGE);
                localTransactionState = LocalTransactionState.ROLLBACK_MESSAGE;
                break;
            case 1:
                //正在执行
                System.out.println("本地事务执行的结果是"+LocalTransactionState.UNKNOW);
                localTransactionState = LocalTransactionState.UNKNOW;
                break;
             default:
                 return null;
        }
        return localTransactionState;
    }
}
