package com.arnold.ecommerce.stream.qinyi;

import com.alibaba.fastjson.JSON;
import com.arnold.ecommerce.vo.QinyiMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

/**
 * <h1>使用自定义的通信信道 QinyiSource 实现消息的发送</h1>
 * */
@Slf4j
@EnableBinding(QinyiSource.class)
public class QinyiSendService {

    private final QinyiSource qinyiSource;

    public QinyiSendService(QinyiSource qinyiSource) {
        this.qinyiSource = qinyiSource;
    }

    /**
     * <h2>使用自定义的输出信道发送消息</h2>
     * */
    public void sendMessage(QinyiMessage message) {

        String _message = JSON.toJSONString(message);
        log.info("in QinyiSendService send message: [{}]", _message);
        qinyiSource.qinyiOutput().send(
                MessageBuilder.withPayload(_message).build()
        );
    }
}
