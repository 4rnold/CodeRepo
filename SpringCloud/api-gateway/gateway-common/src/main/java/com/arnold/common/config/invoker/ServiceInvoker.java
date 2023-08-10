package com.arnold.common.config.invoker;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "channel")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = PushMessageRequest.class, name = "PUSH")
//})
public interface ServiceInvoker {

    String getInvokerPath();

    void setInvokerPath(String invokerPath);

    int getTimeout();

    void setTimeout(int timeout);
}
