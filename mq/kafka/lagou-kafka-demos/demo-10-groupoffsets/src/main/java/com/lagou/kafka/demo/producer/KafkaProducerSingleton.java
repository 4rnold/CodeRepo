package com.lagou.kafka.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Random;

public class KafkaProducerSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerSingleton.class);
    private static KafkaProducer<String, String> kafkaProducer;
    private Random random = new Random();
    private String topic;
    private int retry;

    private KafkaProducerSingleton() {
    }

    /**
     * 静态内部类
     *
     * @author tanjie
     */
    private static class LazyHandler {
        private static final KafkaProducerSingleton instance = new KafkaProducerSingleton();
    }

    /**
     * 单例模式,kafkaProducer是线程安全的,可以多线程共享一个实例
     * @return
     */
    public static final KafkaProducerSingleton getInstance() {
        return LazyHandler.instance;
    }

    /**
     * kafka生产者进行初始化
     *
     * @return KafkaProducer
     */
    public void init(String topic, int retry) {
        this.topic = topic;
        this.retry = retry;
        if (null == kafkaProducer) {
            Properties props = new Properties();
            props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092");
            props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            props.setProperty(ProducerConfig.ACKS_CONFIG, "1");

            kafkaProducer = new KafkaProducer<String, String>(props);
        }
    }

    /**
     * 通过kafkaProducer发送消息
     * @param message
     */
    public void sendKafkaMessage(final String message) {

        ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                topic, random.nextInt(3), "", message);

        kafkaProducer.send(record, new Callback() {
            public void onCompletion(RecordMetadata recordMetadata,
                                     Exception exception) {
                if (null != exception) {
                    LOGGER.error("kafka发送消息失败:" + exception.getMessage(), exception);
                    retryKakfaMessage(message);
                }
            }
        });
    }

    /**
     * 当kafka消息发送失败后,重试
     *
     * @param retryMessage
     */
    private void retryKakfaMessage(final String retryMessage) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                topic, random.nextInt(3), "", retryMessage);
        for (int i = 1; i <= retry; i++) {
            try {
                kafkaProducer.send(record);
                return;
            } catch (Exception e) {
                LOGGER.error("kafka发送消息失败:" + e.getMessage(), e);
                retryKakfaMessage(retryMessage);
            }
        }
    }

    /**
     * kafka实例销毁
     */
    public void close() {
        if (null != kafkaProducer) {
            kafkaProducer.close();
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}