package mayfly.gateway.utils;

import mayfly.core.util.JsonUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author meilin.huang
 * @date 2021-10-12 3:47 下午
 */
public class Utils {
    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param value       响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> write(ServerHttpResponse response, String contentType, HttpStatus status, Object value) {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtils.toJSONString(value).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param value    响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> write(ServerHttpResponse response, Object value) {
        return write(response, MediaType.APPLICATION_JSON_VALUE, HttpStatus.OK, value);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param value    响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> write(ServerHttpResponse response, HttpStatus status, Object value) {
        return write(response, MediaType.APPLICATION_JSON_VALUE, status, value);
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * gateway请求增加请求头信息
     *
     * @param mutate mutate
     * @param name   请求头名
     * @param value  请求头值
     */
    public static void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        mutate.header(name, urlEncode(value.toString()));
    }
}
