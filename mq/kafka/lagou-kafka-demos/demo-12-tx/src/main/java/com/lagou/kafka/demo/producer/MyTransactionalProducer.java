package com.lagou.kafka.demo.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class MyTransactionalProducer {

    public static void main(String[] args) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 提供生产者client.id
        configs.put(ProducerConfig.CLIENT_ID_CONFIG, "tx_producer");

        // 设置事务ID
        configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my_tx_id_1");

        // 需要ISR全体确认消息
        configs.put(ProducerConfig.ACKS_CONFIG, "all");


        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);

        // 初始化事务
        producer.initTransactions();

        try {
            // 开启事务
            producer.beginTransaction();
            // 发送事务消息
            producer.send(new ProducerRecord<>("tp_tx_01", "txkey1", "tx_msg_4"));
            producer.send(new ProducerRecord<>("tp_tx_01", "txkey2", "tx_msg_5"));
            producer.send(new ProducerRecord<>("tp_tx_01", "txkey3", "tx_msg_6"));

            int i = 1 / 0;

            // 提交事务
            producer.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            // 事务回滚
            producer.abortTransaction();
        } finally {
            // 关闭生产者
            producer.close();
        }


    }


}
