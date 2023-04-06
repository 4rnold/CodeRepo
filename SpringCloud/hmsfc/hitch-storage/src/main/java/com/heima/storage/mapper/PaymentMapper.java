package com.heima.storage.mapper;


import com.heima.modules.po.PaymentPO;

import java.util.List;

public interface PaymentMapper {
    int deleteByPrimaryKey(String id);

    int insert(PaymentPO record);

    int insertSelective(PaymentPO record);

    PaymentPO selectByPrimaryKey(String id);

    PaymentPO selectByOrderId(String id);

    List<PaymentPO> selectList(PaymentPO record);

    int updateByPrimaryKeySelective(PaymentPO record);

    int updateByPrimaryKey(PaymentPO record);

}