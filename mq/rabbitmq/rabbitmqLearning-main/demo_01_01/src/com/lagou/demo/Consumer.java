package com.lagou.demo;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    private BlockingQueue<KouZhao> queue;

    public Consumer(BlockingQueue<KouZhao> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(100);

                System.out.println("正在准备买口罩。。。");
                final KouZhao kouZhao = queue.take();
                System.out.println("买到了口罩：" + kouZhao.getId()
                        + " " + kouZhao.getType() + " 口罩");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
