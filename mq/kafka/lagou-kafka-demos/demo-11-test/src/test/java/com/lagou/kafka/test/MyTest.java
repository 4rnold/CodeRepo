package com.lagou.kafka.test;

public class MyTest {
    public static void main(String[] args) {

        String str = "console-consumer-39731";
        // __consumer_offsets的主题分区数量为50个
        System.out.println(Math.abs(str.hashCode()) % 50);

    }
}
