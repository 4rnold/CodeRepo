package com.heima.storage.mapper;


import com.heima.modules.po.OrderPO;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderPO record);

    int insertSelective(OrderPO record);

    OrderPO selectByPrimaryKey(String id);

    List<OrderPO> selectList(OrderPO record);


    List<OrderPO> selectAvailableList(OrderPO record);


    int updateByPrimaryKeySelective(OrderPO record);

    int updateByPrimaryKey(OrderPO record);

    List<OrderPO> selectPaidList(OrderPO orderPO);
}