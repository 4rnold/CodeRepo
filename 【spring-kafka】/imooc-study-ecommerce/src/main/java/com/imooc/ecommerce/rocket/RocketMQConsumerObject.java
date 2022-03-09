package com.imooc.ecommerce.rocket;

import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.vo.QinyiMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * <h1>第四个, RocketMQ 消费者, 指定消费带有 tag 的消息, 且消费的是 Java Pojo</h1>
 * */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "imooc-study-rocketmq",
        consumerGroup = "qinyi-springboot-rocketmq-tag-object",
        selectorExpression = "qinyi"    // 根据 tag 做过滤
)
public class RocketMQConsumerObject implements RocketMQListener<QinyiMessage> {

    @Override
    public void onMessage(QinyiMessage message) {

        log.info("consume message in RocketMQConsumerObject: [{}]",
                JSON.toJSONString(message));
        // so something
    }
}
