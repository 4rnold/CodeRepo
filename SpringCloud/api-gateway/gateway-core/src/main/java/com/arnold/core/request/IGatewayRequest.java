package com.arnold.core.request;

import org.asynchttpclient.Request;
import org.asynchttpclient.cookie.Cookie;

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
