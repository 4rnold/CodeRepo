package com.itheima.test;

import com.itheima.service.BusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext.xml")
public class OrderServiceTest {

    @Autowired
    private BusinessService businessService;

    /**
     * 测试订单支付正常流程
     */
    @Test
    public void testOrderPay(){
        final long start = System.currentTimeMillis();

        String rtValue = businessService.orderPay("1");

        System.out.println("返回值是:"+rtValue+"，消耗时间为:" + (System.currentTimeMillis() - start));
    }


}
