package com.lagou.kafka.demo.service;

import com.alibaba.fastjson.JSON;
import com.lagou.kafka.demo.entity.RetryRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

@Service
public class RetryService {
    private static final Logger log = LoggerFactory.getLogger(RetryService.class);

    /**
     * 消息消费失败后下一次消费的延迟时间(秒)
     * 第一次重试延迟10秒;第	二次延迟30秒,第三次延迟1分钟...
     */
    private static final int[] RETRY_INTERVAL_SECONDS = {10, 30, 1*60, 2*60, 5*60, 10*60, 30*60, 1*60*60, 2*60*60};

    /**
     * 重试topic
     */
    @Value("${spring.kafka.topics.retry}")
    private String retryTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void consumerLater(ConsumerRecord<String, String> record){
        // 获取消息的已重试次数
        int retryTimes = getRetryTimes(record);
        Date nextConsumerTime = getNextConsumerTime(retryTimes);
        // 如果达到重试次数，则不再重试
        if(nextConsumerTime == null) {
            return;
        }

        // 组织消息
        RetryRecord retryRecord = new RetryRecord();
        retryRecord.setNextTime(nextConsumerTime.getTime());
        retryRecord.setTopic(record.topic());
        retryRecord.setRetryTimes(retryTimes);
        retryRecord.setKey(record.key());
        retryRecord.setValue(record.value());

        // 转换为字符串
        String value = JSON.toJSONString(retryRecord);
        // 发送到重试队列
        kafkaTemplate.send(retryTopic, null, value);
    }

    /**
     * 获取消息的已重试次数
     */
    private int getRetryTimes(ConsumerRecord record){
        int retryTimes = -1;
        for(Header header : record.headers()){
            if(RetryRecord.KEY_RETRY_TIMES.equals(header.key())){
                ByteBuffer buffer = ByteBuffer.wrap(header.value());
                retryTimes = buffer.getInt();
            }
        }
        retryTimes++;
        return retryTimes;
    }

    /**
     * 获取待重试消息的下一次消费时间
     */
    private Date getNextConsumerTime(int retryTimes){
        // 重试次数超过上限,不再重试
        if(RETRY_INTERVAL_SECONDS.length < retryTimes) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, RETRY_INTERVAL_SECONDS[retryTimes]);
        return calendar.getTime();
    }
}