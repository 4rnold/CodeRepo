package com.lagou.kafka.demo.consumer;

public class ConsumerAutoMain {
    public static void main(String[] args) {
        KafkaConsumerAuto kafka_consumerAuto = new KafkaConsumerAuto();
        try {
            kafka_consumerAuto.execute();
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            kafka_consumerAuto.shutdown();
        }
    }
}