package com.itheima.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderInfoDao;
import com.itheima.domain.Account;
import com.itheima.domain.Item;
import com.itheima.domain.OrderInfo;
import com.itheima.service.ItemService;
import com.itheima.service.OrderInfoService;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

/***
 *
 *
 ****/
@Service(timeout = 100000,retries = -1)
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Reference(check = false)
    private ItemService itemService;

    /****
     * 创建订单
     * 调用ItemService修改库存(调用AccountService修改余额)
     *
     * @param  usernumber:购买商品的用户
     * @param  id：购买的商品ID
     * @param  count:要减的数量
     */
    @GlobalTransactional(name="seata-itheima-tx")
    @Override
    public int create(String usernumber, String id, Integer count){
        //从数据库查询商品信息
        Item item = new Item();
        item.setId(id);
        item.setNum(count);
        item.setPrice(100L);
        item.setTitle("华为荣耀4");

        //创建订单
        OrderInfo orderInfo = new OrderInfo();
        String orderId = UUID.randomUUID().toString().replace("-","").toUpperCase();
        orderInfo.setId(orderId);
        orderInfo.setMoney(item.getPrice()*count);
        orderInfo.setCreatetime(new Date());
        orderInfo.setUsernumber(usernumber);
        int acount = orderInfoDao.add(orderInfo);

        System.out.println("添加订单受影响行数："+acount);

        //制造异常
//        System.out.println("订单添加成功后,出现异常。。。。");
//        int q=10/0;

        //调用ItemService(远程调用)
        Account account = new Account();
        account.setUsernumber(usernumber);
        account.setMoney(item.getPrice()*count); //花掉的钱
        itemService.update(item,account);

        //制造异常
        System.out.println("开始报错了。。。。");
        int q=10/0;
        return  acount;
    }


}
