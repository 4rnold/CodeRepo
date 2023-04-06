package com.heima.commons.entity;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 回调对象 实现callable接口
 *
 * @param <T>
 */
public class CallbackTask<T> implements Callable<T> {
    //结果对象
    private T result;
    //创建countDownLatch 在前台调用call的时候 hold住线程
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 给其他回调线程执行的call
     * @return
     * @throws Exception
     */
    @Override
    public T call() throws Exception {
        //hold 住线程 其他 响应线程会在这里阻塞
        countDownLatch.await();
        //返回结果
        return result;
    }

    /**
     * 设置结果并唤醒回调线程
     * @param result
     */
    public void setResult(T result) {
        //设置结果
        this.result = result;
        //唤醒响应线程
        countDownLatch.countDown();
    }
}