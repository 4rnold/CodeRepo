package com.tangyh.lamp.gateway.filter.gateway;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.tangyh.basic.base.R;
import com.tangyh.basic.boot.utils.WebUtils;
import com.tangyh.basic.exception.code.ExceptionCode;
import com.tangyh.basic.utils.DateUtils;
import com.tangyh.lamp.gateway.entity.BlockList;
import com.tangyh.lamp.gateway.entity.RateLimiter;
import com.tangyh.lamp.gateway.service.BlockListService;
import com.tangyh.lamp.gateway.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 按规则过滤请求
 * <p>
 * 参考：https://gitee.com/mrbirdd/FEBS-Cloud/blob/master/febs-gateway/src/main/java/cc/mrbird/febs/gateway/common/filter/FebsGatewayRequestFilter.java
 *
 * @author MrBird
 * @author zuihou
 * @date 2020/7/25 下午4:02
 */
@Slf4j
@RequiredArgsConstructor
public class PreCheckFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final BlockListService blockListService;
    private final RateLimiterService rateLimiterService;

    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param value       响应内容
     * @return Mono<Void>
     */
    private static Mono<Void> buildResponse(ServerHttpResponse response, String contentType,
                                            HttpStatus status, Object value) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(value).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 判断是否被阻止访问
        Mono<Void> blockListResult = matchBlockList(exchange);
        if (blockListResult != null) {
            return blockListResult;
        }
        // 判断是否限流
        Mono<Void> rateLimiterResult = matchRateLimiter(exchange);
        if (rateLimiterResult != null) {
            return rateLimiterResult;
        }

        return chain.filter(exchange);
    }

    /**
     * 限流规则匹配
     *
     * @param exchange exchange
     * @return Mono
     */
    public Mono<Void> matchRateLimiter(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        try {
            URI originUri = getUri(exchange);
            if (originUri == null) {
                return null;
            }
            String requestMethod = request.getMethodValue();
            String requestIp = WebUtils.getRemoteAddress(exchange);
            //TODO 发现bug： uri必须和配置的地址完全一致才行。配置地址为 /** 时，无法限流
            RateLimiter rule = rateLimiterService.getRateLimiter(originUri.getPath(), RateLimiter.METHOD_ALL);
            if (rule == null) {
                rule = rateLimiterService.getRateLimiter(originUri.getPath(), requestMethod);
            }
            if (rule != null) {
                AtomicBoolean limit = new AtomicBoolean(false);
                Mono<Void> result = rateLimiterCheck(limit, rule, originUri, requestIp, requestMethod, response);
                log.debug("限流验证已完成");
                if (result != null) {
                    return result;
                }
            }

        } catch (Exception e) {
            log.warn("限流发生异常 : {} ", e.getMessage(), e);
        }
        return null;
    }

    private Mono<Void> rateLimiterCheck(AtomicBoolean limit, RateLimiter rule, URI uri,
                                        String requestIp, String requestMethod, ServerHttpResponse response) {
        boolean isRateLimiterHit = rule.getState()
                && (RateLimiter.METHOD_ALL.equalsIgnoreCase(rule.getRequestMethod()) || StrUtil.equalsIgnoreCase(requestMethod, rule.getRequestMethod()));
        if (isRateLimiterHit) {
            if (StrUtil.isNotBlank(rule.getLimitStart()) && StrUtil.isNotBlank(rule.getLimitEnd())) {
                if (DateUtils.between(LocalTime.parse(rule.getLimitStart()), LocalTime.parse(rule.getLimitEnd()))) {
                    limit.set(true);
                }
            } else {
                limit.set(true);
            }
        }
        if (limit.get()) {
            String requestUri = uri.getPath();
            int count = rateLimiterService.getCurrentRequestCount(requestUri, requestIp);
            if (count == 0) {
                rateLimiterService.setCurrentRequestCount(requestUri, requestIp, rule.getIntervalSec());
            } else if (count >= rule.getCount()) {
                return buildResponse(response, MediaType.APPLICATION_JSON_VALUE,
                        HttpStatus.TOO_MANY_REQUESTS, R.fail(ExceptionCode.UNAUTHORIZED.getCode(), "访问频率超限，请稍后再试"));
            } else {
                rateLimiterService.incrCurrentRequestCount(requestUri, requestIp);
            }
        }
        return null;
    }

    /**
     * 不允许访问的列表
     */
    public Mono<Void> matchBlockList(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        URI originUri = getUri(exchange);
        if (originUri != null) {
            String ip = WebUtils.getRemoteAddress(exchange);
            String requestMethod = request.getMethodValue();
            AtomicBoolean forbid = new AtomicBoolean(false);
            // 阻止访问列表
            Set<Object> blockList = blockListService.findBlockList(ip);
            blockList.addAll(blockListService.findBlockList());

            // 路径和请求方式 能匹配上，且限制区间内则禁用
            doBlockListCheck(forbid, blockList, originUri, requestMethod);

            log.debug("阻止列表验证完成");
            if (forbid.get()) {
                return buildResponse(response, MediaType.APPLICATION_JSON_VALUE,
                        HttpStatus.NOT_ACCEPTABLE, R.fail(ExceptionCode.UNAUTHORIZED.getCode(), "阻止列表限制，禁止访问"));
            }
        } else {
            log.debug("请求地址未正确获取，无法进行阻止列表检查");
        }
        return null;
    }

    private void doBlockListCheck(AtomicBoolean forbid, Set<Object> blockList, URI uri, String requestMethod) {
        for (Object o : blockList) {
            BlockList b = (BlockList) o;
            if (!b.getState()) {
                continue;
            }
            if (!pathMatcher.match(b.getRequestUri(), uri.getPath())) {
                continue;
            }
            if (!BlockList.METHOD_ALL.equalsIgnoreCase(b.getRequestMethod())
                    && !StrUtil.equalsIgnoreCase(requestMethod, b.getRequestMethod())) {
                continue;
            }
            if (StrUtil.isNotBlank(b.getLimitStart()) && StrUtil.isNotBlank(b.getLimitEnd())) {
                if (DateUtils.between(LocalTime.parse(b.getLimitStart()), LocalTime.parse(b.getLimitEnd()))) {
                    forbid.set(true);
                }
            } else {
                forbid.set(true);
            }
            if (forbid.get()) {
                break;
            }
        }
    }

    private URI getUri(ServerWebExchange exchange) {
        return exchange.getRequest().getURI();
    }
}
