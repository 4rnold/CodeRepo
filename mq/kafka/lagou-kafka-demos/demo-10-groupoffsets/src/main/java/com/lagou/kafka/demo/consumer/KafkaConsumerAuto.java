package com.lagou.kafka.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaConsumerAuto {
    /**
     * kafka消费者不是线程安全的
     */
    private final KafkaConsumer<String, String> consumer;

    private ExecutorService executorService;

    public KafkaConsumerAuto() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        // 打开自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put("auto.commit.interval.ms", "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(props);
        // 订阅主题
        consumer.subscribe(Collections.singleton("tp_demo_02"));
    }

    public void execute() throws InterruptedException {
        executorService = Executors.newFixedThreadPool(2);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(2_000);
            if (null != records) {
                executorService.submit(new ConsumerThreadAuto(records, consumer));
            }
            Thread.sleep(1000);
        }
    }

    public void shutdown() {
        try {
            if (consumer != null) {
                consumer.close();
            }
            if (executorService != null) {
                executorService.shutdown();
            }
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("关闭线程池超时。。。");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}