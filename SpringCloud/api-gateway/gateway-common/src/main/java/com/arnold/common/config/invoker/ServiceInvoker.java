package com.arnold.common.config.invoker;

public interface ServiceInvoker {

    String getInvokerPath();

    void setInvokerPath(String invokerPath);

    int getTimeout();

    void setTimeout(int timeout);
}
