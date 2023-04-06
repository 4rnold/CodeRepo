package com.heima.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.modules.bo.PayResultBO;
import com.heima.modules.bo.WXPayBO;
import com.heima.modules.po.OrderPO;
import com.heima.payment.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付工具类型
 */
@Service
public class WXServiceImpl implements PayService {

    @Autowired
    private WXPay wxPay;

    /**
     * 预支付订单
     *
     * @param orderPO
     * @return
     * @throws Exception
     */
    @Override
    public PayResultBO prePay(OrderPO orderPO) throws Exception {
        //包装BO对象 任何金额都是0.01元
        WXPayBO wxPayBO = new WXPayBO(orderPO.getId(), "1", "打车订单", "192.168.64.1");
        //调用微信支付接口
        Map<String, String> resultMap = wxPay.unifiedOrder(wxPayBO.toMap());
        //包装返回结果
        PayResultBO resultBO = new PayResultBO(resultMap);
        //如果异常抛出支付失败的错误
        if (!"SUCCESS".equals(resultBO.getReturnCode())) {
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_COMMUNICATION_FAILURE);
        }
        //如果预支付失败
        if (!"SUCCESS".equals(resultBO.getResultCode())) {
            if (StringUtils.isNotEmpty(resultBO.getErrorMsg())) {
                throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PRE_PAY_FAIL, resultBO.getErrorMsg());
            }
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PRE_PAY_FAIL);
        }
        return resultBO;
    }

    /**
     * 订单查询接口
     *
     * @param orderId
     * @return
     * @throws Exception
     */

    @Override
    public PayResultBO orderQuery(String orderId) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", orderId);
        Map<String, String> resultMap = wxPay.orderQuery(map);
        PayResultBO resultBO = new PayResultBO(resultMap);
        if (!"SUCCESS".equals(resultBO.getReturnCode())) {
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_COMMUNICATION_FAILURE);
        }
        //订单状态未完成
        if (!"SUCCESS".equals(resultBO.getResultCode())) {
            if (StringUtils.isNotEmpty(resultBO.getErrorMsg())) {
                throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PAY_IN_PROGRESSL, resultBO.getErrorMsg());
            }
            throw new BusinessRuntimeException(BusinessErrors.PAYMENT_PAY_IN_PROGRESSL);
        }
        return resultBO;
    }


}
