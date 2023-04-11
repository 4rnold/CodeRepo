package com.itheima.listeners;

import com.itheima.context.pull.PullConfigUtil;
import com.itheima.context.refresh.ContextRefresher;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 监听heimaconfig队列的监听器，它读到消息之后，做更新配置的操作
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Component
//@RabbitListener(queues = "heimaconfig")
@RabbitListener(queuesToDeclare = @Queue("heimaconfig"))
public class ContextRefreshListener {

    @Autowired
    private ContextRefresher contextRefresher;

    /**
     * 读取消息，并处理更新配置的操作
     * @param message
     * @throws Exception
     */
    @RabbitHandler
    public void onMessage(String message)throws Exception{
        if("refresh".equals(message)){
            //1.从服务器端获取最新的配置
            Properties properties = PullConfigUtil.findConfig();//从服务器端拿到的
            //2.调用刷新器，刷新最新的配置
            contextRefresher.refresh(properties);
        }
    }
}
