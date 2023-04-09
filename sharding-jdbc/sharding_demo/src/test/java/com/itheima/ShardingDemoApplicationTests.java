package com.itheima;


import com.itheima.domain.Order;
import com.itheima.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingDemoApplicationTests {

	@Autowired
	private OrderMapper orderMapper;

	@Test
	public void insertTest() {
		for (int i = 0; i < 10; i++) {
			Order order=new Order();
			order.setPrice(new BigDecimal((i+1)*5));
			order.setStatus("wait pay"+"|"+i);
			order.setUserId(1L);
			orderMapper.insertOrder(order);
		}
	}

    /**
     * 读操作，一定是读取的从数据库
     * 根据userId去读取响应的数据库
     */
	@Test
	public void selectPageTest(){
		Long userId=1L;//用户id
		int page=3;//页码
		int size=10;//每页显示的记录数
		int start=(page-1)*size;
        List<Order> orders = orderMapper.selectByPage(userId, start, size);
        System.out.println(orders);
    }
}
