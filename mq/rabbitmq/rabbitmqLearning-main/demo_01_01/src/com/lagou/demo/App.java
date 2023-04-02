package com.lagou.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {
    public static void main(String[] args) {
        BlockingQueue<KouZhao> queue = new ArrayBlockingQueue<>(20);

        new Thread(new Producer(queue)).start();

        new Thread(new Consumer(queue)).start();
    }
}
