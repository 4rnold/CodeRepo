package com.lagou.kafka.demo.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;

public class MyProducer {
    public static void main(String[] args) {

        Map<String, Object> configs = new HashMap<>();
        // bootstrap.servers
        configs.put("bootstrap.servers", "node1:9092");
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092");




        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);





    }
}
