package mayfly.gateway.handler;

import mayfly.core.exception.BaseException;
import mayfly.core.model.result.Result;
import mayfly.gateway.error.GatewayError;
import mayfly.gateway.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关统一异常处理器
 *
 * @author meilin.huang
 * @date 2021-10-12 3:43 下午
 */
@Order(-1000)
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        // 响应状态异常
        if (ex instanceof ResponseStatusException rse) {
            return Utils.write(response, rse.getStatus(), GatewayError.ASSERT_ERROR.toResult(rse.getMessage()));
        }

        if (ex instanceof BaseException re) {
            Result<Object> res = Result.of(re.getErrorCode(), re.getMessage());
            // 如果是权限检验失败，返回401状态码
            if (re.getErrorCode().equals(GatewayError.AUTH_FAIL.getCode())) {
                return Utils.write(response, HttpStatus.UNAUTHORIZED, res);
            }
            return Utils.write(response, res);
        }

        log.error("服务异常", ex);
        return Utils.write(response, HttpStatus.INTERNAL_SERVER_ERROR, GatewayError.SERVER_ERROR.toResult(ex.getMessage()));
    }
}
