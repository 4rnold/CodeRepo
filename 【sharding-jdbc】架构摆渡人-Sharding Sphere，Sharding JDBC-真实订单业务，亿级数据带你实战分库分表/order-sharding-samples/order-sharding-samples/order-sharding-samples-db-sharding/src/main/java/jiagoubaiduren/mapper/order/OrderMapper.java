package jiagoubaiduren.mapper.order;

import jiagoubaiduren.po.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> queryOrderList(Long buyerId);

    List<Order> queryOrderListByStore(Long storeId);

    void createOrder(Order order);

    Order getOrder(String orderNo);
}
