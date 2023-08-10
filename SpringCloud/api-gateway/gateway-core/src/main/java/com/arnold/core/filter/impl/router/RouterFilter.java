package com.arnold.core.filter.impl.router;

import com.arnold.common.constants.FilterConst;
import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.ConnectException;
import com.arnold.common.exception.ResponseException;
import com.arnold.core.ConfigLoader;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;
import com.arnold.core.helper.AsyncHttpHelper;
import com.arnold.core.helper.ResponseHelper;
import com.arnold.core.response.GatewayResponse;
import io.netty.handler.timeout.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@FilterAspect(id = FilterConst.ROUTER_FILTER_ID,
        name = FilterConst.ROUTER_FILTER_NAME,
        order = FilterConst.ROUTER_FILTER_ORDER)
public class RouterFilter implements Filter {
    @Override
    public void doFilter(GatewayContext context) {
        route(context);
    }


    private void route(GatewayContext gatewayContext) {
        //创建asynchttpclient的request
        //这里应该应用rules了
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

    }}
