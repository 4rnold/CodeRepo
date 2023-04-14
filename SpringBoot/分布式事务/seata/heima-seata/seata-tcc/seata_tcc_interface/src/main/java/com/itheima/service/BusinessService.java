package com.itheima.service;

import com.itheima.domain.Order;

import java.math.BigDecimal;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface BusinessService {

    /**
     * 外部服务：下单
     * @param orderId 订单Id
     * @return
     */
    String orderPay(String orderId);
}
