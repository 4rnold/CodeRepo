package com.arnold.core.netty.processor;

import com.arnold.core.LifeCycle;
import com.arnold.core.request.HttpRequestWrapper;

public interface NettyProcessor extends LifeCycle {
    void process(HttpRequestWrapper httpRequestWrapper);
}
