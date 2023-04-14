package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.commons.emums.OrderStatusEnum;
import com.itheima.dao.OrderDao;
import com.itheima.domain.Order;
import com.itheima.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;


@Service(timeout = 100000,retries = -1)
public class OrderServiceImpl implements OrderService {



    @Autowired
    private OrderDao orderDao;



    @Override
    public boolean prepareUpdateState(BusinessActionContext businessActionContext,String  orderId) {
        int res = orderDao.updateStatus(orderId,OrderStatusEnum.PAYING.getCode());
        String xid = RootContext.getXID();
        System.out.println("order "+xid);
        return res > 0 ? Boolean.TRUE : Boolean.FALSE;
    }



    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        System.out.println("更新订单状态为支付成功");
        String orderId = ((String)businessActionContext.getActionContext("orderId"));
        int res = orderDao.updateStatus(orderId,OrderStatusEnum.PAY_SUCCESS.getCode());
        return res > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        System.out.println("更新订单状态为支付失败");
        String orderId = ((String)businessActionContext.getActionContext("orderId"));
        int res = orderDao.updateStatus(orderId,OrderStatusEnum.PAY_FAIL.getCode());
        return res > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Order findById(String id) {
        return orderDao.findById(id);
    }
}
