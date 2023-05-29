package com.lagou.kafka.demo;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.requests.DescribeLogDirsResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MyAdminClient {

    private KafkaAdminClient client;

    @Before
    public void before() {

        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", "node1:9092");
        configs.put("client.id", "admin_001");

        client = (KafkaAdminClient) KafkaAdminClient.create(configs);
    }

    @After
    public void after() {
        // 关闭admin客户端
        client.close();
    }

    @Test
    public void testListTopics() throws ExecutionException, InterruptedException {
        // 列出主题
//        final ListTopicsResult listTopicsResult = client.listTopics();

        ListTopicsOptions options = new ListTopicsOptions();
        // 列出内部主题
        options.listInternal(true);
        // 设置请求超时时间，单位是毫秒
        options.timeoutMs(500);

        final ListTopicsResult listTopicsResult = client.listTopics(options);

//        final Set<String> strings = listTopicsResult.names().get();
//
//        strings.forEach(name -> {
//            System.out.println(name);
//        });

        // 将请求变成同步的请求，直接获取结果
        final Collection<TopicListing> topicListings = listTopicsResult.listings().get();

        topicListings.forEach(new Consumer<TopicListing>() {
            @Override
            public void accept(TopicListing topicListing) {

                // 该主题是否是内部主题
                final boolean internal = topicListing.isInternal();
                // 该主题的名字
                final String name = topicListing.name();


                System.out.println("主题是否是内部主题：" + internal);
                System.out.println("主题的名字：" + name);
                System.out.println(topicListing);
                System.out.println("=====================================");
            }
        });

    }


    @Test
    public void testDescribeLogDirs() throws ExecutionException, InterruptedException {
        final DescribeLogDirsResult describeLogDirsResult = client.describeLogDirs(Collections.singleton(0));

        final Map<Integer, Map<String, DescribeLogDirsResponse.LogDirInfo>> integerMapMap
                = describeLogDirsResult.all().get();

        integerMapMap.forEach(new BiConsumer<Integer, Map<String, DescribeLogDirsResponse.LogDirInfo>>() {
            @Override
            public void accept(Integer integer, Map<String, DescribeLogDirsResponse.LogDirInfo> stringLogDirInfoMap) {
                System.out.println("broker.id = " + integer);
//                log.dirs可以设置多个目录
                stringLogDirInfoMap.forEach(new BiConsumer<String, DescribeLogDirsResponse.LogDirInfo>() {
                    @Override
                    public void accept(String s, DescribeLogDirsResponse.LogDirInfo logDirInfo) {
                        System.out.println("logdir = " + s);
                        final Map<TopicPartition, DescribeLogDirsResponse.ReplicaInfo> replicaInfos = logDirInfo.replicaInfos;

                        replicaInfos.forEach(new BiConsumer<TopicPartition, DescribeLogDirsResponse.ReplicaInfo>() {
                            @Override
                            public void accept(TopicPartition topicPartition, DescribeLogDirsResponse.ReplicaInfo replicaInfo) {
                                System.out.println("主题分区：" + topicPartition.partition());
                                System.out.println("主题：" + topicPartition.topic());
//                                final boolean isFuture = replicaInfo.isFuture;
//                                final long offsetLag = replicaInfo.offsetLag;
//                                final long size = replicaInfo.size;
                            }
                        });

                    }
                });
            }
        });


    }





}
