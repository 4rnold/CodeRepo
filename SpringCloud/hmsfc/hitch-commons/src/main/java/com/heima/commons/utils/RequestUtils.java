package com.heima.commons.utils;

import com.heima.commons.constant.HtichConstants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestUtils {

    /**
     * 获取Request对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    /**
     * 获取Head中的数据
     *
     * @param headerName
     * @return
     */
    public static String getRequestHeader(String headerName) {
        HttpServletRequest request = getRequest();
        return request.getHeader(headerName);
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    public static String getCurrentUserId() {
        return getRequestHeader(HtichConstants.HEADER_ACCOUNT_KEY);
    }

}
