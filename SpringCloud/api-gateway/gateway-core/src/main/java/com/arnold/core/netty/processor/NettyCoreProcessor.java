package com.arnold.core.netty.processor;

import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.BaseException;
import com.arnold.common.exception.ConnectException;
import com.arnold.common.exception.ResponseException;
import com.arnold.core.ConfigLoader;
import com.arnold.core.context.GatewayContext;
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

    //传俩参数不就行了，不用wrapper包装
    @Override
    public void process(HttpRequestWrapper httpRequestWrapper) {
        FullHttpRequest request = httpRequestWrapper.getRequest();
        ChannelHandlerContext ctx = httpRequestWrapper.getCtx();

        try {
            GatewayContext gatewayContext = RequestHelper.buildGatewayContext(request, ctx);
            route(gatewayContext);
        } catch (BaseException e) {
            log.error("process error {} {}", e.getCode().getCode(), e.getCode().getMessage());
            FullHttpResponse response = ResponseHelper.getHttpResponse(e.getCode());
            doWriteAndRelease(ctx,request,response);
        } catch (Exception e) {
            log.error("process error {}", e.getMessage());
            FullHttpResponse response = ResponseHelper.getHttpResponse(ResponseCode.INTERNAL_ERROR);
            doWriteAndRelease(ctx,request,response);
        }
    }

    private void doWriteAndRelease(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        ReferenceCountUtil.release(request);
    }

    private void route(GatewayContext gatewayContext) {
        //创建asynchttpclient的request
        Request request = gatewayContext.getRequest().build();
        CompletableFuture<Response> future = AsyncHttpHelper.getInstance().executeRequest(request);
        boolean whenComplete = ConfigLoader.getConfig().isWhenComplete();
        //同步异步
        if (whenComplete) {
            future.whenComplete(((response, throwable) -> {
                complete(request, response, throwable, gatewayContext);
            }));
        } else {
            future.whenCompleteAsync(((response, throwable) -> {
                complete(request, response, throwable, gatewayContext);
            }));
        }
    }

    private void complete(Request request, Response response, Throwable throwable, GatewayContext gatewayContext) {
        if (Objects.nonNull(throwable)) {
            String url = request.getUrl();
            if (throwable instanceof TimeoutException) {
                log.warn("request timeout {}", url);
                gatewayContext.setThrowable(
                        new ResponseException(ResponseCode.REQUEST_TIMEOUT)
                );
            } else {
                gatewayContext.setThrowable(
                        new ConnectException(throwable, gatewayContext.getUniqueId(), url,
                                ResponseCode.HTTP_RESPONSE_ERROR)
                );
            }
        } else {
            gatewayContext.setResponse(GatewayResponse.buildGatewayResponse(response));
        }
        gatewayContext.writtened();
        ResponseHelper.writeResponse(gatewayContext);

    }
}
