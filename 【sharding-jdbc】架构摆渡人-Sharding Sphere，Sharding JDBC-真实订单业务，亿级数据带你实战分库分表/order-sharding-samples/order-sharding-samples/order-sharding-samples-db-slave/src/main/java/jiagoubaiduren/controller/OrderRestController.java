package jiagoubaiduren.controller;

import jiagoubaiduren.response.OrderResponse;
import jiagoubaiduren.service.OrderService;
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
}
