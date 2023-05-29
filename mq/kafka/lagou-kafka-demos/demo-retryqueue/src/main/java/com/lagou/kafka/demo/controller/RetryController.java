package com.lagou.kafka.demo.controller;

import com.lagou.kafka.demo.service.KafkaService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class RetryController {

    @Autowired
    private KafkaService kafkaService;

    @Value("${spring.kafka.topics.test}")
    private String topic;

    @RequestMapping("/send/{message}")
    public String sendMessage(@PathVariable String message) throws ExecutionException, InterruptedException {

        ProducerRecord<String, String> record = new ProducerRecord<>(
                topic,
                message
        );

        // 向业务主题发送消息
        String result = kafkaService.sendMessage(record);

        return result;
    }

}