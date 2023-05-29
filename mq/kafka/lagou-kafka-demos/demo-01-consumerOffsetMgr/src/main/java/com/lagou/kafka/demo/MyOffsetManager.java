package com.lagou.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.function.BiConsumer;

public class MyOffsetManager {
    public static void main(String[] args) {

        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // group.id很重要
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "mygrp1");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);

//        consumer.subscribe(Collections.singleton("tp_demo_01"));

        // 如何手动给消费者分配分区？
        // 1、需要知道有哪些主题可以访问，和消费

        // 获取当前消费者可以访问和消费的主题以及它们的分区信息
//        final Map<String, List<PartitionInfo>> stringListMap = consumer.listTopics();
//
//        stringListMap.forEach(new BiConsumer<String, List<PartitionInfo>>() {
//            @Override
//            public void accept(String topicName, List<PartitionInfo> partitionInfos) {
//                System.out.println("主题名称：" + topicName);
//                for (PartitionInfo partitionInfo : partitionInfos) {
//                    System.out.println(partitionInfo);
//                }
//            }
//        });

//        final Set<TopicPartition> assignment1 = consumer.assignment();
//
//        for (TopicPartition partition : assignment1) {
//            System.out.println(partition);
//        }
//        System.out.println("----------------------------");

        // 给当前消费者分配指定的主题分区
        consumer.assign(Arrays.asList(
                new TopicPartition("tp_demo_01", 0),
                new TopicPartition("tp_demo_01", 1),
                new TopicPartition("tp_demo_01", 2)
        ));

        // 获取给当前消费者分配的主题分区信息
//        final Set<TopicPartition> assignment = consumer.assignment();
//
//        for (TopicPartition partition : assignment) {
//            System.out.println(partition);
//        }

        // 查看当前消费者在指定主题的分区上的消费者偏移量
//        final long offset0 = consumer.position(new TopicPartition("tp_demo_01", 0));
//
//        System.out.println("当前主题在0号分区上的位移：" + offset0);

//        consumer.seekToBeginning(Arrays.asList(
//                new TopicPartition("tp_demo_01", 0),
//                new TopicPartition("tp_demo_01", 2)
//        ));
        long offset0 = consumer.position(new TopicPartition("tp_demo_01", 0));
        long offset1 = consumer.position(new TopicPartition("tp_demo_01", 1));
        long offset2 = consumer.position(new TopicPartition("tp_demo_01", 2));

        System.out.println(offset0);
        System.out.println(offset1);
        System.out.println(offset2);

//        consumer.seekToEnd(Arrays.asList(new TopicPartition("tp_demo_01", 2)));

        consumer.seek(new TopicPartition("tp_demo_01", 2), 14);

        offset0 = consumer.position(new TopicPartition("tp_demo_01", 0));
        offset1 = consumer.position(new TopicPartition("tp_demo_01", 1));
        offset2 = consumer.position(new TopicPartition("tp_demo_01", 2));

        System.out.println(offset0);
        System.out.println(offset1);
        System.out.println(offset2);

        consumer.close();
    }



}
