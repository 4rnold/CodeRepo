package com.arnold.core.request;

import io.netty.handler.codec.http.cookie.Cookie;
import org.asynchttpclient.Request;

public interface IGatewayRequest {

    void setModifyHost(String modifyHost);

    String getModifyHost();

    void setModifyPath(String modifyPath);

    String getModifyPath();

    void addHeader(String key, String value);

    String getHeader(String key);

    void addQueryParameter(String key, String value);

    void addFormParameter(String key, String value);

    void addOrReplaceCookie(Cookie cookie);

    void setRequestTimeout(int timeout);

    String getFinalUrl();

    Request build();

}
