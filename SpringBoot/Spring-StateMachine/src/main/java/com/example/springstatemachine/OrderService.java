package com.example.springstatemachine;

public interface OrderService {
    Order getById(Long id);

    Order create(Order order);

    Order pay(Long id);

    Order deliver(Long id);

    Order receive(Long id);

}
