package com.lagou.kafka.demo.entity;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RetryRecord {

    public static final String KEY_RETRY_TIMES = "retryTimes";

    private String key;
    private String value;

    private Integer retryTimes;
    private String topic;
    private Long nextTime;

    public RetryRecord() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getNextTime() {
        return nextTime;
    }

    public void setNextTime(Long nextTime) {
        this.nextTime = nextTime;
    }

    public ProducerRecord parse() {
        Integer partition = null;
        Long timestamp = System.currentTimeMillis();
        List<Header> headers = new ArrayList<>();
        ByteBuffer retryTimesBuffer = ByteBuffer.allocate(4);
        retryTimesBuffer.putInt(retryTimes);
        retryTimesBuffer.flip();
        headers.add(new RecordHeader(RetryRecord.KEY_RETRY_TIMES, retryTimesBuffer));

        ProducerRecord sendRecord = new ProducerRecord(
                topic, partition, timestamp, key, value, headers);
        return sendRecord;
    }
}