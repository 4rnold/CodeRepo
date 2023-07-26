package com.arnold.common.config.invoker;

public class AbstractServiceInvoker implements ServiceInvoker {

    private String invokerPath;

    private int timeout;

    @Override
    public String getInvokerPath() {
        return invokerPath;
    }

    @Override
    public void setInvokerPath(String invokerPath) {
        this.invokerPath = invokerPath;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
