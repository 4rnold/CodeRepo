package com.arnold.ecommerce.stream.custom;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <h1>自定义输出信道</h1>
 * */
public interface QinyiSource {

    String OUTPUT = "qinyiOutput";

    /** 输出信道的名称是 qinyiOutput, 需要使用 Stream 绑定器在 yml 文件中声明 */
    @Output(QinyiSource.OUTPUT)
    MessageChannel qinyiOutput();
}
