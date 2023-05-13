package com.crazymakercircle.netty.basic;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.util.Logger;
import com.crazymakercircle.util.ThreadUtil;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;


public class NettyReactorTest {

    @Test
    public void testJUCThreadPool() {

        ExecutorService pool = Executors.newFixedThreadPool(2);
        Runnable runTarget = new Runnable() {
            @Override
            public void run() {
                Logger.tcfo(" i am execute by  thread pool");
            }
        };

        for (int i = 0; i < 10; i++) {
            pool.submit(runTarget);

        }

        ThreadUtil.sleepSeconds(1000);
    }

    @Test
    public void testNettyThreadPool() {

//        ExecutorService pool = new DefaultEventLoop();
        ExecutorService pool = new NioEventLoopGroup(2);
        Runnable runTarget = new Runnable() {
            @Override
            public void run() {
                Logger.tcfo(" i am execute by  thread pool");
            }
        };
        for (int i = 0; i < 10; i++) {
            pool.submit(runTarget);

        }

        ThreadUtil.sleepSeconds(1000);
    }

    @Test
    public void testJUCscheduleAtFixedRate() {

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        Runnable runTarget = new Runnable() {
            @Override
            public void run() {
                Logger.tcfo(" i am execute by  thread pool");
            }
        };
        for (int i = 0; i < 10; i++) {
            pool.scheduleAtFixedRate(runTarget, 1, 1, TimeUnit.MINUTES);

        }

        ThreadUtil.sleepSeconds(1000);
    }

    @Test
    public void testNettyscheduleAtFixedRate() {

        ScheduledExecutorService pool = new NioEventLoopGroup(2);
        Runnable runTarget = new Runnable() {
            @Override
            public void run() {
                Logger.tcfo(" i am execute by  thread pool");
            }
        };
        for (int i = 0; i < 10; i++) {
            ((ScheduledExecutorService) pool).scheduleAtFixedRate(runTarget, 10, 10, TimeUnit.SECONDS);

        }

        ThreadUtil.sleepSeconds(1000);
    }

    @Test
    public void testUserbuilder() {
        User user = User.builder()
                .devId("卷王01")
                .name("疯狂创客圈 卷王")
                .platform(17)
                .build();
        System.out.println(user);
    }


    static class IntegerWrapper {
        Integer i = 0;
        ReentrantLock lock;

        public IntegerWrapper() {
            lock = new ReentrantLock();
        }

        public Integer getValue() {
            return i;
        }


    }

    static class Task implements Runnable {
        IntegerWrapper integerWrapper = null;
        CountDownLatch latch;

        public Task(IntegerWrapper i, CountDownLatch latch) {
            this.integerWrapper = i;
            this.latch = latch;

        }

        @Override
        public void run() {
            integerWrapper.lock.lock();
            try {
                integerWrapper.i++;
            } finally {
                integerWrapper.lock.unlock();
            }

            latch.countDown();
        }

        @Override
        public String toString() {
            return integerWrapper.toString();
        }
    }


    @Test
    public void testThreadPoolTaskSchedule() {

        IntegerWrapper[] integerWrappers = new IntegerWrapper[100];

        //500w次，8个线程 ，juc 程序运行时间： 19567ms
        //500w次，8个线程 ，juc 程序运行时间： 19s
        //1000w次，16个线程 ，juc 程序运行时间： 33101ms
        //1000w次，16个线程 ，juc 程序运行时间： 33s

//        Integer countPerInt = 50000;
        Integer countPerInt = 100000;

        CountDownLatch latch = new CountDownLatch(countPerInt * 100);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            integerWrappers[i] = new IntegerWrapper();
        }

//        ThreadPoolExecutor pool = ThreadUtil.getCpuIntenseTargetThreadPool();
        ExecutorService pool = Executors.newFixedThreadPool(16);
        for (int j = 0; j < countPerInt; j++) {
            for (int i = 0; i < 100; i++) {
                IntegerWrapper integerWrapper = integerWrappers[i];
                pool.submit(new Task(integerWrapper, latch));

            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
        System.out.println("程序运行时间： " + (endTime - startTime) / 1000 + "s");

        for (int i = 0; i < 100; i++) {
            IntegerWrapper integerWrapper = integerWrappers[i];
            if (integerWrapper.i < countPerInt) {
                System.err.printf(i + "  error:" + integerWrapper.getValue());
            }
        }
    }


    //对应的netty 的通道

    static class IntegerWrapperNoneLock {
        Integer i = 0;
        EventExecutor eventloop;


        public Integer getValue() {
            return i;
        }

        public void register(EventExecutor eventloop) {
            this.eventloop=eventloop;

        }
    }

    static class TaskNoneLock implements Runnable {
        IntegerWrapperNoneLock integerWrapper = null;
        CountDownLatch latch;

        public TaskNoneLock(IntegerWrapperNoneLock i, CountDownLatch latch) {
            this.integerWrapper = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            integerWrapper.i++;
            latch.countDown();
        }

        @Override
        public String toString() {
            return integerWrapper.toString();
        }
    }

    @Test
    public void testThreadPoolOfNetty() {

        IntegerWrapperNoneLock[] integerWrappers = new IntegerWrapperNoneLock[100];

        // 500次，8个线程 ，juc 程序运行时间： 19567ms
        // 500次，8个线程 ，juc 程序运行时间： 19s
        // 500次，8个线程 ，netty 程序运行时间： 16587ms
        // 500次，8个线程 ，netty 程序运行时间： 16s

        //1000w次，16个线程 ，juc 程序运行时间： 33101ms
        //1000w次，16个线程 ，juc 程序运行时间： 33s
        //
        // 1000w次，16个线程 ，netty 程序运行时间：  29848ms
        //1000w次，16个线程 ，netty 程序运行时间： 29s
//        Integer countPerInt = 50000;
        Integer countPerInt = 100000;

        CountDownLatch latch = new CountDownLatch(countPerInt * 100);

        long startTime = System.currentTimeMillis();

        //创建netty  线程池
        DefaultEventLoopGroup pool = new DefaultEventLoopGroup(16);
//        ThreadPoolExecutor pool = ThreadUtil.getCpuIntenseTargetThreadPool();

        for (int i = 0; i < 100; i++) {
            integerWrappers[i] = new IntegerWrapperNoneLock();

            EventExecutor eventloop = pool.next();

            integerWrappers[i].register(eventloop);

        }


        for (int j = 0; j < countPerInt; j++) {

            for (int i = 0; i < 100; i++) {

                IntegerWrapperNoneLock integerWrapper = integerWrappers[i];

                EventExecutor eventloop = integerWrapper.eventloop;

                eventloop.submit(new TaskNoneLock(integerWrapper, latch));

            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
        System.out.println("程序运行时间： " + (endTime - startTime) / 1000 + "s");

        for (int i = 0; i < 100; i++) {
            IntegerWrapperNoneLock integerWrapper = integerWrappers[i];
            if (integerWrapper.i < countPerInt) {
                System.err.printf(i + "  error:" + integerWrapper.getValue());
            }
        }
    }


}