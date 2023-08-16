package com.arnold.core.filter.impl.flowCtl.Rule.CountLimiter;

public interface CountLimiter {

    boolean doFlowCtl(String key, int limit, int expireTime);
}
