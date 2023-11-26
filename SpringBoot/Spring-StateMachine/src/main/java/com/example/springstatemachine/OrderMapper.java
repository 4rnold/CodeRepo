package com.example.springstatemachine;


import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderMapper {

  private Map<Integer,Order> orderMap = new ConcurrentHashMap<>();


    public void insert(Order order) {
        orderMap.put(order.getId(),order);
    }

    public Order selectById(Integer id) {
        return orderMap.get(id);
    }

    public void updateById(Order order) {
        orderMap.put(order.getId(),order);
    }
}
