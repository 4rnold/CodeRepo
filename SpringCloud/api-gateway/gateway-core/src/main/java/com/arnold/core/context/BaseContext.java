package com.arnold.core.context;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@SuperBuilder
@Data
public class BaseContext implements IContext{

    //转发协议
    private String protocol;

    //状态
    private volatile int status = IContext.Running;

    //netty 上下文
    private ChannelHandlerContext channelHandlerContext;

    //异常
    private Throwable throwable;

    private boolean keepAlive;

    protected List<Consumer<IContext>> completedCallBack;

    protected final Map<String, Object> attributes = new HashMap<>();

    protected  final AtomicBoolean requestReleased = new AtomicBoolean(false);


    public BaseContext(String protocol, ChannelHandlerContext channelHandlerContext, boolean keepAlive) {
        this.protocol = protocol;
        this.channelHandlerContext = channelHandlerContext;
        this.keepAlive = keepAlive;
    }

    @Override
    public void runned() {
        status = IContext.Running;
    }

    @Override
    public void writtened() {
        status = IContext.Written;
    }

    @Override
    public void completed() {
        status = IContext.Completed;
    }

    @Override
    public void terminated() {
        status = IContext.Terminated;
    }

    @Override
    public boolean isRunning() {
        return status == IContext.Running;
    }

    @Override
    public boolean isWritten() {
        return status == IContext.Written;
    }

    @Override
    public boolean isCompleted() {
        return status == IContext.Completed;
    }

    @Override
    public boolean isTerminated() {
        return status == IContext.Terminated;
    }

    @Override
    public Object getRequest() {
        return null;
    }

    @Override
    public Object getResponse() {
        return null;
    }

    @Override
    public Object setResponse(Object response) {
        return null;
    }

    @Override
    public void releaseRequest() {
    }

    @Override
    public void setCompletedCallBack(Consumer<IContext> consumer) {
        if(completedCallBack == null){
            completedCallBack = new ArrayList<>();
        }
        completedCallBack.add(consumer);
    }

    @Override
    public void invokeCompletedCallBack() {
        if(completedCallBack != null){
            for(Consumer<IContext> consumer : completedCallBack){
                consumer.accept(this);
            }
        }
    }
}
