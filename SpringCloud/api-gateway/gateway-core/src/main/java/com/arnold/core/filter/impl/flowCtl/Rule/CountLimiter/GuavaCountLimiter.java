package com.arnold.core.filter.impl.flowCtl.Rule.CountLimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;

public class GuavaCountLimiter  implements CountLimiter {

    private RateLimiter rateLimiter;

    //guava都是按每分钟算

    public GuavaCountLimiter() {
    }

    public GuavaCountLimiter(double maxPermitsPerSec) {
        this.rateLimiter= RateLimiter.create(maxPermitsPerSec);
    }

    private static ConcurrentHashMap<String,GuavaCountLimiter> resourceRateLimiterMap = new ConcurrentHashMap<>();

    @Override
    public boolean doFlowCtl(String key, int limitPermit, int limitDuration) {
        GuavaCountLimiter guavaCountLimiter = resourceRateLimiterMap.get(key);
        if (guavaCountLimiter == null) {
            int count = (int) Math.ceil(limitPermit/(limitDuration/1000));
            guavaCountLimiter = resourceRateLimiterMap.putIfAbsent(key, new GuavaCountLimiter(count));
        }
        return guavaCountLimiter.acquire(1);
    }

//    public static GuavaCountLimiter getInstance(String key) {
//        GuavaCountLimiter guavaRateLimiterInstance = resourceRateLimiterMap.get(key);
//        if (guavaRateLimiterInstance == null) {
//            guavaRateLimiterInstance = new GuavaCountLimiter();
//            resourceRateLimiterMap.putIfAbsent(key, guavaRateLimiterInstance);
//        }
//        return guavaRateLimiterInstance;
//    }

    private boolean acquire(int permits) {
        boolean b = rateLimiter.tryAcquire(permits);
        System.out.println(b);
        return b;
    }
}
