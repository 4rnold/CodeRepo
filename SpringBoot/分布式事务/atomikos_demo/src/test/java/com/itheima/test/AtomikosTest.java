package com.itheima.test;


import com.itheima.domain.Stock;
import com.itheima.service.StockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AtomikosTest {

    @Autowired
    private StockService stockService;

    @Test
    public void testSave(){
        //1.创建对象
        Stock stock = new Stock();
        //2.填充数据
        String id = UUID.randomUUID().toString().replace("-","").toUpperCase();
        stock.setId(id);
        stock.setProductId("1030102301111");
        stock.setProductName("华为p8");
        stock.setQuantity(100);
        //3.执行保存
        stockService.save(stock);
    }
}
