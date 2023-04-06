package com.heima.payment.handler;

import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.utils.CommonsUtils;
import com.heima.commons.utils.reflect.ReflectUtils;
import com.heima.modules.bo.PayResultBO;
import com.heima.modules.po.OrderPO;
import com.heima.modules.po.PaymentPO;
import com.heima.modules.vo.OrderVO;
import com.heima.modules.vo.PaymentVO;
import com.heima.payment.service.OrderAPIService;
import com.heima.payment.service.PayService;
import com.heima.payment.service.PaymentAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentHandler {
    @Autowired
    private PayService payService;

    @Autowired
    private PaymentAPIService paymentAPIService;

    @Autowired
    private OrderAPIService orderAPIService;

    /**
     * 预支付接口
     *
     * @param paymentVO
     * @return
     * @throws Exception
     */
    public ResponseVO<PaymentVO> prePay(PaymentVO paymentVO) throws Exception {
        OrderPO orderPO = checkOrder(paymentVO);
        PayResultBO payResultBO = payService.prePay(orderPO);
        addPayOrder(paymentVO, payResultBO);
        ReflectUtils.copyProperties(payResultBO, paymentVO);
        return ResponseVO.success(paymentVO);
    }

    /**
     * 订单查询
     *
     * @param paymentVO
     * @return
     * @throws Exception
     */

    public ResponseVO<OrderVO> orderQuery(PaymentVO paymentVO) throws Exception {
        OrderPO orderPO = checkOrder(paymentVO);
        PaymentPO paymentPO = paymentAPIService.selectByOrderId(orderPO.getId());
        if (paymentPO == null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        //如果支付未完成
        if (orderPO.getStatus() == 1) {
            //查询支付状态
            PayResultBO payResultBO = payService.orderQuery(orderPO.getId());
            //如果支付成功修改订单状态
            if (null != payResultBO) {
                paymentPO.setPayInfo(payResultBO.getPayInfo());
                if (payResultBO.isSuccess()) {
                    updateOrderPaySucces(paymentPO, orderPO);
                }
            }
        }
        return ResponseVO.success(orderPO, paymentPO.getPayInfo());
    }

    /**
     * 确认支付
     *
     * @param paymentVO
     */
    public ResponseVO<OrderVO> confirmPay(PaymentVO paymentVO) {
        OrderPO orderPO = checkOrder(paymentVO);
        PaymentPO paymentPO = paymentAPIService.selectByOrderId(orderPO.getId());
        if (paymentPO == null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        //如果支付未完成
        if (orderPO.getStatus() == 1) {
            //如果支付成功修改订单状态
            paymentPO.setPayInfo("支付成功");
            updateOrderPaySucces(paymentPO, orderPO);
        }
        return ResponseVO.success(orderPO);
    }


    /**
     * 添加支付订单
     *
     * @param paymentVO
     */
    private void addPayOrder(PaymentVO paymentVO, PayResultBO payResultBO) {
        PaymentPO paymentPO = CommonsUtils.toPO(paymentVO);
        paymentPO.setPrepayId(payResultBO.getPrepayId());
        paymentPO.setAmount(1F);
        paymentPO.setChannel(1);
        paymentPO.setTransactionOrderNum("1");
        paymentAPIService.add(paymentPO);
    }

    /**
     * 更新订单数据未支付成功
     *
     * @param paymentPO
     * @param orderPO
     */
    public void updateOrderPaySucces(PaymentPO paymentPO, OrderPO orderPO) {
        paymentAPIService.update(paymentPO);
        orderPO.setStatus(2);
        orderAPIService.update(orderPO);
    }

    public OrderPO checkOrder(PaymentVO paymentVO) {
        OrderPO orderPO = orderAPIService.selectByID(paymentVO.getOrderId());
        if (null == orderPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户订单不存在");
        }
        return orderPO;
    }


}
