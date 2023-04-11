package com.itheima.listeners;

import com.itheima.disastertolerance.DisasterToleranceStrategy;
import com.itheima.domain.ConfigInfo;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Component
//@RabbitListener(queues = "synchronizationdfs")
@RabbitListener(queuesToDeclare = @Queue("synchronizationdfs"))
public class SynchronizationDFSListener {

    @Autowired
    private DisasterToleranceStrategy disasterToleranceStrategy;

    /**
     * 处理监听方法
     * @param map
     */
    @RabbitHandler
    public void synchronizationDFS(Map<String,Object> map){
        //1.取出map中的数据
        String operated = (String)map.get("operated");
        ConfigInfo configInfo = (ConfigInfo)map.get("data");
        //2.判断是什么操作
        if("change".equalsIgnoreCase(operated)){
            //保存（先删除，再保存的）
            disasterToleranceStrategy.saveDfs(configInfo);
        }else if("del".equalsIgnoreCase(operated)){
            //删除
            disasterToleranceStrategy.removeDfs(configInfo.getId());
        }
    }
}
