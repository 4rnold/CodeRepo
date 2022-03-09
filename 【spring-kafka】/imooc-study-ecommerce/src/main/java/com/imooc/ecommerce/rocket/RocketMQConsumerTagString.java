package com.imooc.ecommerce.rocket;

import com.alibaba.fastjson.JSON;
import com.imooc.ecommerce.vo.QinyiMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * <h1>第二个 RocketMQ 消费者, 指定了消费带有 tag 的消息</h1>
 * */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = "imooc-study-rocketmq",
        consumerGroup = "qinyi-springboot-rocketmq-tag-string",
        selectorExpression = "qinyi"        // 根据 tag 过滤
)
public class RocketMQConsumerTagString implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {

        QinyiMessage rocketMessage = JSON.parseObject(message, QinyiMessage.class);
        log.info("consume message in RocketMQConsumerTagString: [{}]",
                JSON.toJSONString(rocketMessage));
    }
}
