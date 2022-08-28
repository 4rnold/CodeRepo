package jiagoubaiduren.service;

import jiagoubaiduren.common.IdWorker;
import jiagoubaiduren.dao.OrderDao;
import jiagoubaiduren.dao.StoreOrderDao;
import jiagoubaiduren.po.Order;
import jiagoubaiduren.po.StoreOrder;
import jiagoubaiduren.response.OrderResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private StoreOrderDao storeOrderDao;


    public List<OrderResponse> queryOrderList(Long buyerId) {
        return orderDao.queryOrderList(buyerId).stream().map(order -> {
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);
            return response;
        }).collect(Collectors.toList());
    }

    public List<OrderResponse> queryOrderListByStore(Long storeId) {
        return orderDao.queryOrderListByStore(storeId).stream().map(order -> {
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);
            return response;
        }).collect(Collectors.toList());
    }

    public List<OrderResponse> queryOrderListByStore2(Long storeId) {
        return storeOrderDao.queryOrderListByStore(storeId).stream().map(order -> {
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);
            return response;
        }).collect(Collectors.toList());
    }

    public OrderResponse getOrder(String orderNo) {
        Order order = orderDao.getOrder(orderNo);
        if (Objects.isNull(order)) {
            throw new NullPointerException("订单不存在");
        }
        OrderResponse response = new OrderResponse();
        BeanUtils.copyProperties(order, response);
        return response;
    }


    public String createOrder(Order order) {
        String orderNo = generateOrderNo(order.getBuyerId());
        order.setOrderNo(orderNo);
        orderDao.createOrder(order);
        // 用binlog监听解耦更合适
        StoreOrder storeOrder = new StoreOrder();
        BeanUtils.copyProperties(order, storeOrder);
        storeOrderDao.createOrder(storeOrder);
        return orderNo;
    }

    private String generateOrderNo(Long buyerId) {
        String uid = buyerId.toString();
        if (uid.length() > 4) {
            uid = uid.substring(uid.length() - 4);
        } else {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < 4 - uid.length(); i++) {
                str.append("0");
            }
            str.append(uid);
            uid = str.toString();
        }
        IdWorker worker = new IdWorker(1,1,1);
        return worker.nextId() + "" + uid;
    }
}
