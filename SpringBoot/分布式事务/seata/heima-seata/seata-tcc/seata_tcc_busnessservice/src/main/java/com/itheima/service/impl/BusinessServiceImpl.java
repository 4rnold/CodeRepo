package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.commons.emums.OrderStatusEnum;
import com.itheima.domain.Order;
import com.itheima.service.AccountService;
import com.itheima.service.BusinessService;
import com.itheima.service.InventoryService;
import com.itheima.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.transaction.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    @Reference
    private OrderService orderService;

    @Reference
    private InventoryService inventoryService;

    @Reference
    private AccountService accountService;

    @Override
    @GlobalTransactional(name = "seata-tcc-tx",rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public String orderPay(String orderId){
        String xid = RootContext.getXID();
        System.out.println(xid);
        //1.根据订单id查询订单
        Order order = orderService.findById(orderId);
        //2.判断是否有此订单
        if(order != null && order.getStatus().intValue() == OrderStatusEnum.NOT_PAY.getCode()) {
            //3.更新订单状态为支付中
            orderService.prepareUpdateState(null, orderId);
            //4.预扣减余额（冻结账户资金）
            accountService.preparePayment(null,order.getUserId(),order.getTotalAmount());
            //5.预扣减库存（锁定库存）
            inventoryService.prepareDecrease(null,order.getProductId(), order.getCount());
        }else {
            return "订单不存在";
        }
        return "success";
    }
}
