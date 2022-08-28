package jiagoubaiduren.service;

import jiagoubaiduren.dao.OrderDao;
import jiagoubaiduren.response.OrderResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public List<OrderResponse> queryOrderList(Long buyerId) {
        return orderDao.queryOrderList(buyerId).stream().map(order -> {
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);
            return response;
        }).collect(Collectors.toList());
    }
}
