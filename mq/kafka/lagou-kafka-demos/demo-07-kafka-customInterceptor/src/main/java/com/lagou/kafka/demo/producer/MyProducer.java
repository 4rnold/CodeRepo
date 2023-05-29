package com.lagou.kafka.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class MyProducer {
    public static void main(String[] args) {

        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 保证等待确认的消息只有设置的这几个。如果设置为1，则只有一个请求在等待响应
        // 此时可以保证发送消息即使在重试的情况下也是有序的。
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
//        configs.put("max.in.flight.requests.per.connection", 1);

//        interceptor.classes
        // 如果有多个拦截器，则设置为多个拦截器类的全限定类名，中间用逗号隔开
        configs.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.lagou.kafka.demo.interceptor.InterceptorOne," +
                "com.lagou.kafka.demo.interceptor.InterceptorTwo," +
                "com.lagou.kafka.demo.interceptor.InterceptorThree");


        configs.put("classContent", "this is lagou's kafka class");

        KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(configs);

        ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(
                "tp_inter_01",
                0,
                1001,
                "this is lagou's 1001 message"
        );

        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println(metadata.offset());
                }
            }
        });

        // 关闭生产者
        producer.close();


    }
}
