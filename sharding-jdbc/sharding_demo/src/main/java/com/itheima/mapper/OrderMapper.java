package com.itheima.mapper;

import com.itheima.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/*
订单mapper接口
 */
@Mapper
public interface OrderMapper {

    /*
        插入订单
        问题1：在执行sql的时候t_order是什么？为什么不是t_order_1或者t_order_2?
            为什么不是t_order_1或者t_order_2?
                因为我们需求是主键为偶数的进入t_order_1，主键为奇数的进入t_order_2表中
            为什么是t_order呢？
                首先t_order是逻辑表，而且t_order不是随便写的，要和分片规则中配置的逻辑表名一样
     */
    @Insert("insert into t_order(price,user_id,status) values(#{price},#{userId},#{status})")
    public int insertOrder(Order order);

    /*
        分页查询订单
     */
    @Select("select * from t_order where user_id=#{userId} order by order_id desc limit #{start},#{size}")
    public List<Order> selectByPage(@Param("userId") Long userId,@Param("start") int start,@Param("size") int size);
}
