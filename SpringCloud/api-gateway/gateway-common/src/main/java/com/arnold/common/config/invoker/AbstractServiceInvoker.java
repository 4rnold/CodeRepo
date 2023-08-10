package com.arnold.common.config.invoker;

import com.arnold.common.config.dubboInvoker.DubboServiceInvoker;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HttpServiceInvoker.class, name = "1"),
        @JsonSubTypes.Type(value = DubboServiceInvoker.class, name = "2")
})
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
