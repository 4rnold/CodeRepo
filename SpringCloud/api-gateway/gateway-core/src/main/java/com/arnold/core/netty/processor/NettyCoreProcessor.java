package com.arnold.core.netty.processor;

import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.BaseException;
import com.arnold.common.exception.ConnectException;
import com.arnold.common.exception.ResponseException;
import com.arnold.core.ConfigLoader;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.FilterChainFactory;
import com.arnold.core.filter.FilterChainFactoryImpl;
import com.arnold.core.filter.GatewayFilterChain;
import com.arnold.core.helper.AsyncHttpHelper;
import com.arnold.core.helper.RequestHelper;
import com.arnold.core.helper.ResponseHelper;
import com.arnold.core.request.HttpRequestWrapper;
import com.arnold.core.response.GatewayResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class NettyCoreProcessor implements NettyProcessor {

    private FilterChainFactory filterfactory = FilterChainFactoryImpl.getInstance();
    //传俩参数不就行了，不用wrapper包装
    @Override
    public void process(HttpRequestWrapper httpRequestWrapper) {
        FullHttpRequest request = httpRequestWrapper.getRequest();
        ChannelHandlerContext ctx = httpRequestWrapper.getCtx();

        try {
            GatewayContext gatewayContext = RequestHelper.buildGatewayContext(request, ctx);

            //执行过滤器逻辑
            GatewayFilterChain gatewayFilterChain = filterfactory.buildFilterChain(gatewayContext);

            gatewayFilterChain.doFilter(gatewayContext);


            //route(gatewayContext);
        } catch (BaseException e) {
            log.error("process error {} {}", e.getCode().getCode(), e.getCode().getMessage());
            FullHttpResponse response = ResponseHelper.getHttpResponse(e.getCode());
            doWriteAndRelease(ctx,request,response);
        } catch (Exception e) {
            log.error("process error {}", e.getMessage());
            e.printStackTrace();
            FullHttpResponse response = ResponseHelper.getHttpResponse(ResponseCode.INTERNAL_ERROR);
            doWriteAndRelease(ctx,request,response);
        }
    }

    private void doWriteAndRelease(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        ReferenceCountUtil.release(request);
    }


}
