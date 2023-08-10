package com.arnold.core.request;

import com.arnold.common.constants.BasicConst;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
public class GatewayRequest implements IGatewayRequest {

    private final String targetServiceUniqueId;


    //开始时间
    private final long startTime;

    //字符集
    @Getter
    private final Charset charset;

    //clientip
    @Getter
    private final String clientIp;

    //host
    @Getter
    private final String host;

    //path
    @Getter
    private final String path;

    //uri
    @Getter
    private final String uri;

    //httpmethod
    @Getter
    private final HttpMethod httpMethod;

    //contenttype
    @Getter
    private final String contentType;

    //netty httpHeader
    @Getter
    private final HttpHeaders httpHeaders;

    //参数解析器
    @Getter
    private final QueryStringDecoder queryStringDecoder;

    //fullhttprequest
    @Getter
    private final FullHttpRequest fullHttpRequest;

    //body
    @Getter
    private String body;

    private Map<String, Cookie> cookieMap;

    //post parameter
    @Getter
    private Map<String, List<String>> postParameterMap;

    //modifySchema
    @Getter
    private String modifySchema;

    //modifyHost
    private String modifyHost;

    //modifyPath
    @Getter
    private String modifyPath;

    private final RequestBuilder requestBuilder;

    /*
     * jwt解析出的userid
     */
    private long userId;

    public GatewayRequest(String targetServiceUniqueId,
                          Charset charset, String clientIp, String host,
                          String uri, HttpMethod httpMethod,
                          String contentType, HttpHeaders httpHeaders,
                          FullHttpRequest fullHttpRequest) {
        this.targetServiceUniqueId = targetServiceUniqueId;
        //now
        this.startTime = Calendar.getInstance().getTimeInMillis();
//        this.endTime = endTime;
        this.charset = charset;
        this.clientIp = clientIp;
        this.host = host;

        this.uri = uri;
        this.httpMethod = httpMethod;
        this.contentType = contentType;
        this.httpHeaders = httpHeaders;

        //QueryStringDecoder 的作用就是把 HTTP uri 分割成 path 和 key-value 参数对，
        // 也可以用来解码 Content-Type = "application/x-www-form-urlencoded" 的 HTTP POST。
        // 特别注意的是，该 decoder 仅能使用一次。
        this.queryStringDecoder = new QueryStringDecoder(uri, charset);
        this.fullHttpRequest = fullHttpRequest;


        this.path = queryStringDecoder.path();

        this.modifyHost = host;
        this.modifyPath = path;
        this.modifySchema = BasicConst.HTTP_PREFIX_SEPARATOR;

        this.requestBuilder = new RequestBuilder();
        requestBuilder.setMethod(getHttpMethod().name());
        requestBuilder.setHeaders(getHttpHeaders());
        requestBuilder.setQueryParams(queryStringDecoder.parameters());
        ByteBuf content = fullHttpRequest.content();
        if (content != null) {
            requestBuilder.setBody(content.nioBuffer());
        }

    }

    //getbody
    public String getBody() {
        if (StringUtils.isEmpty(body)) {
            body = getFullHttpRequest().content().toString(charset);
        }
        return body;
    }

    //getcookie
    public Cookie getCookie(String key) {
        if (cookieMap == null) {
            cookieMap = new HashMap<>();
            String cookieStr = getHttpHeaders().get(HttpHeaderNames.COOKIE);
            //??
            Set<Cookie> Cookies = ServerCookieDecoder.STRICT.decode(cookieStr);
            for (Cookie cookie : Cookies) {
                cookieMap.put(key, cookie);
            }
        }
        return cookieMap.get(key);
    }

    //获取指定名称的参数值
    public List<String> getQueryParameter(String key) {
        return getQueryStringDecoder().parameters().get(key);
    }

    //getPostParametersByName
    public List<String> getPostParametersByName(String key) {
        String body = getBody();
        if (isFormPost()) {
            if (postParameterMap == null) {
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(body, false);
                postParameterMap = queryStringDecoder.parameters();
            }
            if (postParameterMap.isEmpty()) {
                return null;
            } else {
                return postParameterMap.get(key);
            }
        } else if (isJsonPost()) {
            return Lists.newArrayList(JsonPath.read(body, key).toString());
        }
        return null;
    }

    private boolean isFormPost() {
        return HttpMethod.POST.equals(getHttpMethod()) &&(
                    contentType.startsWith(HttpHeaderValues.FORM_DATA.toString())||
                    contentType.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                );
    }

    //isJsonPost
    private boolean isJsonPost() {
        return HttpMethod.POST.equals(getHttpMethod()) &&(
                    contentType.startsWith(HttpHeaderValues.APPLICATION_JSON.toString())
                );
    }

    @Override
    public void addHeader(String key, String value) {
        requestBuilder.addHeader(key, value);
    }

    @Override
    public String getHeader(String key) {
        return null;
    }

    @Override
    public void addQueryParameter(String key, String value) {
        requestBuilder.addQueryParam(key, value);
    }

    @Override
    public void addFormParameter(String key, String value) {
        if (isFormPost()) {
            requestBuilder.addFormParam(key, value);
        }
    }

    @Override
    public void addOrReplaceCookie(org.asynchttpclient.cookie.Cookie cookie) {
        requestBuilder.addOrReplaceCookie(cookie);
    }

    @Override
    public void setRequestTimeout(int timeout) {
        requestBuilder.setRequestTimeout(timeout);
    }

    @Override
    public String getFinalUrl() {
        return modifySchema + modifyHost + modifyPath;
    }

    @Override
    public Request build() {
        requestBuilder.setUrl(getFinalUrl());
        requestBuilder.setHeader("userId", String.valueOf(userId));

        return requestBuilder.build();
    }
}
