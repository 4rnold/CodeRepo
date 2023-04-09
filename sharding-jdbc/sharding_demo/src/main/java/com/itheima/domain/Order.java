package com.itheima.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private Long orderId;
    private BigDecimal price;
    private Long userId;
    private String status;
}
