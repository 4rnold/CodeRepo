package com.crazymakercircle.cocurrent;


/**
 * Created by 尼恩 at 疯狂创客圈
 */

import com.crazymakercircle.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class FutureTaskScheduler
{
    static ThreadPoolExecutor mixPool = null;

    static {
        mixPool = ThreadUtil.getMixedTargetThreadPool();
    }

    private FutureTaskScheduler()
    {

    }

    /**
     * 添加任务
     *
     * @param executeTask
     */


    public static void add(Runnable executeTask) {
        mixPool.submit(()->{ executeTask.run(); });
    }

}
