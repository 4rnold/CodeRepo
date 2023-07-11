package mayfly.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import mayfly.auth.api.AuthRemoteService;
import mayfly.auth.api.model.req.AuthDTO;
import mayfly.auth.api.model.res.AuthResDTO;
import mayfly.core.constant.HeaderConst;
import mayfly.core.exception.BizAssert;
import mayfly.core.model.result.Result;
import mayfly.core.permission.LoginAccount;
import mayfly.core.util.StringUtils;
import mayfly.gateway.config.NoAuthUrlsConfig;
import mayfly.gateway.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 网关鉴权过滤器
 *
 * @author meilin.huang
 * @date 2021-10-09 5:10 下午
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthRemoteService authRemoteService;
    @Autowired
    private NoAuthUrlsConfig noAuthUrlsConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 获取真实请求服务及路径，如 lb://mayfly-sys/path
        URI realUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        BizAssert.notNull(realUri, "获取服务请求地址失败");
        String service = realUri.getHost();
        String path = realUri.getPath();

        ServerHttpRequest.Builder mutate = request.mutate();
        // 不需要权限校验则直接通过
        if (noAuthUrlsConfig.match(service, path)) {
            return chain.filter(exchange.mutate().request(mutate.build()).build());
        }

        String token = request.getHeaders().getFirst(HeaderConst.TOKEN_NAME);
        if (StringUtils.isEmpty(token)) {
            // header 未获取到，则从query param里获取
            token = request.getQueryParams().getFirst(HeaderConst.TOKEN_NAME);
        }

        String finalToken = token;
        try {
            Result<AuthResDTO> res = CompletableFuture.supplyAsync(() -> authRemoteService.auth(new AuthDTO()
                            .setService(service)
                            .setPath(path)
                            .setMethod(request.getMethodValue())
                            .setToken(finalToken)))
                    .get();
            LoginAccount loginAccount = res.tryGet().getLoginAccount();
            if (loginAccount != null) {
                // 将登录账号信息赋值到header，方便内部服务获取登录账号基本信息
                Utils.addHeader(mutate, HeaderConst.ACCOUNT_ID, loginAccount.getId());
                Utils.addHeader(mutate, HeaderConst.ACCOUNT_USERNAME, loginAccount.getUsername());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("调用auth服务鉴权失败", e);
            throw BizAssert.newException("权限校验失败");
        }

        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        // 在负载均衡转换前一步执行该过滤器，为了获取路由的请求服务及路径
        return ReactiveLoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER - 1;
    }
}
