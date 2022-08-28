package jiagoubaiduren.controller;

import jiagoubaiduren.po.Order;
import jiagoubaiduren.response.OrderResponse;
import jiagoubaiduren.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/list")
    public List<OrderResponse> queryOrderList(Long buyerId) {
        return orderService.queryOrderList(buyerId);
    }

    @GetMapping("/order/list2")
    public List<OrderResponse> queryOrderListByStore2(Long storeId) {
        return orderService.queryOrderListByStore(storeId);
    }

    @GetMapping("/order/list3")
    public List<OrderResponse> queryOrderListByStore3(Long storeId) {
        return orderService.queryOrderListByStore2(storeId);
    }

    @GetMapping("/order/create")
    public String createOrder(Long buyerId, Long storeId) {
        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setStoreId(storeId);
        return orderService.createOrder(order);
    }

    @GetMapping("/order/detail")
    public OrderResponse getOrder(String orderNo) {
        return orderService.getOrder(orderNo);
    }

}
