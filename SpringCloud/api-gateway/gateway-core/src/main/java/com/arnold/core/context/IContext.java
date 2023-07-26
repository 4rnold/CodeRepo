package com.arnold.core.context;

import io.netty.channel.ChannelHandlerContext;

import java.util.function.Consumer;

public interface IContext {

    int Running = 1;

    int Written = 0;

    int Completed = 1;

    int Terminated = 2;

    void runned();

    void writtened();

    void completed();

    void terminated();

    boolean isRunning();
    boolean isWritten();
    boolean isCompleted();
    boolean isTerminated();

    String getProtocol();

    Object getRequest();
    Object getResponse();
    Throwable getThrowable();

    Object setResponse(Object response);
    void setThrowable(Throwable throwable);

    ChannelHandlerContext getChannelHandlerContext();

    boolean isKeepAlive();

    void releaseRequest();

    void setCompletedCallBack(Consumer<IContext> consumer);

    void invokeCompletedCallBack();


}
