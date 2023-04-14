package com.itheima.dao;

import com.itheima.domain.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface OrderDao {


    /**
     * 更新订单
     * @param orderId 订单id
     * @param status  订单状态
     * @return rows
     */
    @Update("update `order` set status = #{status}  where id=#{id}")
    int updateStatus(@Param("id") String orderId,@Param("status") Integer status);

    /**
     * 根据id查询订单
     * @param orderId 订单id
     * @return order
     */
    @Select("select * from  `order` where id = #{id}")
    @Results(id = "orderMap", value = {
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "number",column = "number"),
            @Result(property = "status",column = "status"),
            @Result(property = "productId",column = "product_id"),
            @Result(property = "totalAmount",column = "total_amount"),
            @Result(property = "count",column = "count"),
            @Result(property = "userId",column = "user_id"),
            @Result(property = "id",column = "id",id = true),
    })
    Order  findById(String orderId);
}
