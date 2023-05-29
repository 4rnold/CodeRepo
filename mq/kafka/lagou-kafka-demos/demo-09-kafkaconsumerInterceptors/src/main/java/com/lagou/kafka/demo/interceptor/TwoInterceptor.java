package com.lagou.kafka.demo.interceptor;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public class TwoInterceptor implements ConsumerInterceptor<String, String> {
    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
        // poll方法返回结果之前最后要调用的方法

        System.out.println("Two -- 开始");

        // 消息不做处理，直接返回
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        // 消费者提交偏移量的时候，经过该方法
        System.out.println("Two -- 结束");
    }

    @Override
    public void close() {
        // 用于关闭该拦截器用到的资源，如打开的文件，连接的数据库等
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // 用于获取消费者的设置参数
        configs.forEach((k, v) -> {
            System.out.println(k + "\t" + v);
        });
    }
}
