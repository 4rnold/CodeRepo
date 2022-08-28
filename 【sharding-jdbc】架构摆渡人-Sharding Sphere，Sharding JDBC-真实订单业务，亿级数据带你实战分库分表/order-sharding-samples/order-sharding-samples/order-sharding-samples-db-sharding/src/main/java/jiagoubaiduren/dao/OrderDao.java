package jiagoubaiduren.dao;

import jiagoubaiduren.aspect.SqlReadSlave;
import jiagoubaiduren.mapper.order.OrderMapper;
import jiagoubaiduren.po.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao {

    @Autowired
    private OrderMapper orderMapper;

    //@SqlReadSlave
    public List<Order> queryOrderList(Long buyerId) {
        return orderMapper.queryOrderList(buyerId);
    }

    public List<Order> queryOrderListByStore(Long storeId) {
        return orderMapper.queryOrderListByStore(storeId);
    }

    public void createOrder(Order order) {
        orderMapper.createOrder(order);
    }

    public Order getOrder(String orderNo) {
        return orderMapper.getOrder(orderNo);
    }
}
