package com.arnold.core.netty;

import com.arnold.core.netty.processor.NettyProcessor;
import com.arnold.core.request.HttpRequestWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    private NettyProcessor nettyProcessor;

    public NettyHttpServerHandler(NettyProcessor nettyProcessor) {
        this.nettyProcessor = nettyProcessor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest request = (FullHttpRequest) msg;
        HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper();
        httpRequestWrapper.setRequest(request);
        httpRequestWrapper.setCtx(ctx);

        nettyProcessor.process(httpRequestWrapper);
    }
}
