package com.arnold.core.netty.processor;

import com.arnold.common.enums.ResponseCode;
import com.arnold.core.Config;
import com.arnold.core.disruptor.EventListener;
import com.arnold.core.disruptor.ParallelQueue;
import com.arnold.core.disruptor.ParallelQueueHandler;
import com.arnold.core.helper.ResponseHelper;
import com.arnold.core.request.HttpRequestWrapper;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DisruptorNettyCoreProcessor implements NettyProcessor {

    private static final String THREAD_NAME_PREFIX = "gateway-disruptor-queue-";

    //代理对象
    private NettyProcessor nettyProcessor;

    private ParallelQueue parallelQueue;

    public DisruptorNettyCoreProcessor(Config config, NettyProcessor nettyProcessor) {
        this.nettyProcessor = nettyProcessor;
        parallelQueue = new ParallelQueueHandler<HttpRequestWrapper>(10,
                "",
                new NettyRequestEventListener(),
                ProducerType.MULTI,
                config.getDisruptor_bufferSize(),
                config.getDisruptor_waitStrategy()
                );
    }

    @Override
    public void process(HttpRequestWrapper httpRequestWrapper) {
        //直接扔到disruptor中.
        parallelQueue.add(httpRequestWrapper);
    }


    @Override
    public void init() {

    }

    @Override
    public void start() {
        parallelQueue.start();
    }

    @Override
    public void shutdown() {
        parallelQueue.shutDown();
    }

    public class NettyRequestEventListener implements EventListener<HttpRequestWrapper> {
        //从队列里出来了,交给原来的Processor处理.
        @Override
        public void onEvent(HttpRequestWrapper event) {
            nettyProcessor.process(event);
        }

        @Override
        public void onException(Throwable throwable, long sequence, HttpRequestWrapper event) {
            FullHttpRequest request = event.getRequest();
            ChannelHandlerContext ctx = event.getCtx();
            log.error("disruptor process request{} error:{}", request, throwable);

            FullHttpResponse httpResponse = ResponseHelper.getHttpResponse(ResponseCode.INTERNAL_ERROR);
            if (!HttpUtil.isKeepAlive(request)) {
                ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
            } else {
//                httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.writeAndFlush(httpResponse);
            }

        }
    }

}
