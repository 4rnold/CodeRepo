package com.example.springstatemachine;


import lombok.Data;

@Data
public class Order {

    private Integer id;

    private Integer status;

//    public void setStatus(Integer key) {
//        state = key;
//    }
//
//    public Integer getId() {
//        return id;
//    }
}
