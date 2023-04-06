package com.heima.commons.utils;


import com.heima.commons.entity.Round;

import java.util.function.Supplier;

public class PollingRound {
    //轮询操作类型
    public enum TYPE {
        CONTINUE, EXIT
    }

    /**
     * 轮询操作
     *
     * @param supplier
     * @param delayTime
     * @param <T>
     * @return
     */
    public static <T> T pollingPull(Supplier<Round<T>> supplier, long delayTime) {
        //自旋
        while (true) {
            //获取一个轮次
            Round<T> round = supplier.get();
            //如果返回null
            if (round == null) {
                //延迟指定时间
                CommonsUtils.delay(delayTime);
                //继续
                continue;
            }
            //如果是继续
            if (round.getType() == PollingRound.TYPE.CONTINUE) {
                //获取延时时间
                long customDelayTime = round.getDelayTime() > 0 ? round.getDelayTime() : delayTime;
                //延迟
                CommonsUtils.delay(customDelayTime);
                //继续
                continue;
            }
            //如果是EXIT类型 退出轮询 并返回结果
            return round.getResult();
        }
    }

    /**
     * 轮询操作
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T pollingPull(Supplier<Round<T>> supplier) {
        return pollingPull(supplier, 5);
    }

    public static Round loop() {
        return new Round(PollingRound.TYPE.CONTINUE);
    }

    public static Round delayLoop(long delay) {
        return new Round(PollingRound.TYPE.CONTINUE, null, delay);
    }

    public static <T> Round<T> exit(T result) {
        return new Round(PollingRound.TYPE.EXIT, result);
    }

}
