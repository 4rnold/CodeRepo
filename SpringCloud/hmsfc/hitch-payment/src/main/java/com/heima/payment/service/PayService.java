package com.heima.payment.service;

import com.heima.modules.bo.PayResultBO;
import com.heima.modules.po.OrderPO;

public interface PayService {
    /**
     * 预支付接口
     * @param orderPO
     * @return
     * @throws Exception
     */
    public PayResultBO prePay(OrderPO orderPO) throws Exception;

    /**
     * 订单查询
     * @param orderId
     * @return
     */
    public PayResultBO orderQuery(String orderId) throws Exception;


}
