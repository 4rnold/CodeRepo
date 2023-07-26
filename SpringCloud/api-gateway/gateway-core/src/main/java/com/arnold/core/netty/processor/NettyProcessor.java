package com.arnold.core.netty.processor;

import com.arnold.core.request.HttpRequestWrapper;

public interface NettyProcessor {
    void process(HttpRequestWrapper httpRequestWrapper);
}
