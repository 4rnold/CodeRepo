package com.heima.gateway.configuration;

import com.heima.commons.constant.HtichConstants;
import com.heima.commons.entity.SessionContext;
import com.heima.commons.helper.RedisSessionHelper;
import io.netty.buffer.ByteBufAllocator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class TokenAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenAuthGatewayFilterFactory.PathConfig> {

    @Autowired
    private RedisSessionHelper redisSessionHelper;

    TokenAuthGatewayFilterFactory() {
        super(PathConfig.class);
    }


    public boolean verifyPassPath(RequestPath requestPath, PathConfig pathConfig) {
        String[] passPathArray = pathConfig.getPathArray();
        if (null == passPathArray) {
            return false;
        }
        if (passPathArray.length == 0) {
            return false;
        }
        List<String> filterList = Arrays.stream(passPathArray).filter(path -> {
            if (path.equals(requestPath.toString())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (!filterList.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return new ArrayList<String>() {{
            add("path");
        }};
    }

    @Override
    public GatewayFilter apply(PathConfig config) {
        return (exchange, chain) -> {
            // 获取request和response，注意：不是HttpServletRequest及HttpServletResponse
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            RequestPath requestPath = request.getPath();
            String sessionToken = getSessionToken(request);
            //验证放行路径
            if (verifyPassPath(requestPath, config)) {
                // 认证通过放行
                return chain.filter(exchange);
            }
            //非空判断
            if (StringUtils.isEmpty(sessionToken)) {
                // 响应未认证！
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                // 结束请求
                return response.setComplete();

            }
            SessionContext context = redisSessionHelper.getSession(sessionToken);
            boolean isisValid = redisSessionHelper.isValid(context);
            //session已经失效
            if (!isisValid) {
                // 响应未认证！
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                // 结束请求
                return response.setComplete();
            }
            String accountID = context.getAccountID();
            exchange.getRequest().mutate().headers(httpHeaders -> {
                httpHeaders.add(HtichConstants.HEADER_ACCOUNT_KEY, accountID);
            });
            // 认证通过放行
            return chain.filter(exchange);
        };

    }

    public static class PathConfig {
        private String path;
        private String[] pathArray;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            if (StringUtils.isNotEmpty(path)) {
                pathArray = path.split(";");
            }
            this.path = path;
        }

        public String[] getPathArray() {
            return pathArray;
        }
    }

    /**
     * 获取Token信息
     *
     * @param request
     * @return
     */
    private static String getSessionToken(ServerHttpRequest request) {
        String sessionToken = request.getHeaders().getFirst(HtichConstants.SESSION_TOKEN_KEY);
        if (StringUtils.isEmpty(sessionToken)) {
            sessionToken = request.getQueryParams().getFirst(HtichConstants.SESSION_TOKEN_KEY);
        }
        return sessionToken;
    }


}
