package com.arnold.core.filter.impl.router;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.arnold.common.constants.FilterConst;
import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.ConnectException;
import com.arnold.common.exception.ResponseException;
import com.arnold.common.rule.Rule;
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

import java.io.IOException;
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
        //注意：sentinel不生效。
        try (Entry entry = SphU.asyncEntry(request.getUrl())) {
            CompletableFuture<Response> future = AsyncHttpHelper.getInstance().executeRequest(request);
            boolean whenComplete = ConfigLoader.getConfig().isWhenComplete();
            //同步异步
            if (whenComplete) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                future.whenComplete(((response, throwable) -> {
                    if (Objects.nonNull(throwable)) {
                        Tracer.traceEntry(throwable, entry);
                    }
                    complete(request, response, throwable, gatewayContext);
                }));
            } else {
                future.whenCompleteAsync(((response, throwable) -> {
                    complete(request, response, throwable, gatewayContext);
                }));
            }
        } catch (BlockException e) {
            throw new ResponseException(ResponseCode.SENTINEL_Fallback);
        }

    }

    private void complete(Request request, Response response, Throwable throwable, GatewayContext gatewayContext) {
        if (Objects.nonNull(throwable)) {
            //重试
            int currentRetryTimes = gatewayContext.getCurrentRetryTimes();
            Rule.RetryConfig retryConfig = gatewayContext.getRule().getRetryConfig();
            int retryTimes = retryConfig.getRetryTimes();

            //重试
            if (throwable instanceof TimeoutException || throwable instanceof IOException/*ConnejctjException*/ || throwable instanceof java.util.concurrent.TimeoutException) {
                if (currentRetryTimes < retryTimes) {
                    doRetry(gatewayContext, currentRetryTimes);
                    return;
                }
            }

            ResponseCode responseCode = null;
            //不重试
            String url = request.getUrl();
            if (throwable instanceof TimeoutException) {
                log.warn("request timeout {}", url);
                gatewayContext.setThrowable(
                        new ResponseException(ResponseCode.REQUEST_TIMEOUT)
                );
                responseCode = ResponseCode.REQUEST_TIMEOUT;
            } else if (throwable instanceof java.util.concurrent.TimeoutException) {
                log.warn("request timeout {}", url);
                gatewayContext.setThrowable(
                        new ResponseException(ResponseCode.REQUEST_TIMEOUT)
                );
                responseCode = ResponseCode.REQUEST_TIMEOUT;
            } else {
                gatewayContext.setThrowable(
                        new ConnectException(throwable, gatewayContext.getUniqueId(), url,
                                ResponseCode.HTTP_RESPONSE_ERROR)
                );
                responseCode = ResponseCode.HTTP_RESPONSE_ERROR;
            }

            gatewayContext.setResponse(GatewayResponse.buildGatewayResponse(responseCode));
        } else {
            //没有异常
            gatewayContext.setResponse(GatewayResponse.buildGatewayResponse(response));
        }
        gatewayContext.writtened();
        ResponseHelper.writeResponse(gatewayContext);

    }


    private void doRetry(GatewayContext gatewayContext, int currentRetryTimes) {
        log.info("第{}次重试", currentRetryTimes+1);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gatewayContext.setCurrentRetryTimes(currentRetryTimes + 1);
        doFilter(gatewayContext);
    }
}
