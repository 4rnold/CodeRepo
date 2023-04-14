package com.itheima.dao;

import com.itheima.domain.OrderInfo;
import org.apache.ibatis.annotations.Insert;

/***
 *
 *
 ****/
public interface OrderInfoDao {

    /***
     * 添加订单
     * @param orderInfo
     * @return
     */
    @Insert("insert into order_info(id,money,createtime,usernumber) values(#{id},#{money},#{createtime},#{usernumber})")
    int add(OrderInfo orderInfo);



}
