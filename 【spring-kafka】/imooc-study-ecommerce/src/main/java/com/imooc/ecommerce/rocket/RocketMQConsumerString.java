package com.imooc.ecommerce.rocket;

import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.vo.QinyiMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * <h1>第一个 RocketMQ 消费者</h1>
 * */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "imooc-study-rocketmq",
        consumerGroup = "qinyi-springboot-rocketmq-string"
)
public class RocketMQConsumerString implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {

        QinyiMessage rocketMessage = JSON.parseObject(message, QinyiMessage.class);
        log.info("consume message in RocketMQConsumerString: [{}]",
                JSON.toJSONString(rocketMessage));
    }
}
