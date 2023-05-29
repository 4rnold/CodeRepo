package com.lagou.kafka.demo.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KafkaService {

    private Logger log = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(ProducerRecord<String, String> record) throws ExecutionException, InterruptedException {

        SendResult<String, String> result = this.kafkaTemplate.send(record).get();
        RecordMetadata metadata = result.getRecordMetadata();
        String returnResult = metadata.topic() + "\t" + metadata.partition() + "\t" + metadata.offset();
        log.info("发送消息成功：" + returnResult);

        return returnResult;
    }

}