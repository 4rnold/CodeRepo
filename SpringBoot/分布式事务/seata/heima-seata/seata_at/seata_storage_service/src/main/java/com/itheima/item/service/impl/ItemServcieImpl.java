package com.itheima.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.ItemDao;
import com.itheima.domain.Account;
import com.itheima.domain.Item;
import com.itheima.service.AccountService;
import com.itheima.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

/***
 *
 *
 ****/
@Service(timeout = 100000,retries = -1)
public class ItemServcieImpl implements ItemService {


    @Autowired
    private ItemDao itemDao;

    @Reference(check = false)
    private AccountService accountService;


    /****
     * 修改商品库存，同时修改账户余额
     * @param item
     * @param account
     * @return
     */
    @Override
    public int update(Item item, Account account){
        //修改商品的库存
        int mcount = itemDao.update(item);
        System.out.println("更新库存受影响行数："+mcount);
//        int i = 1/0;
        //修改账户余额
        int acount = accountService.update(account);
        System.out.println("账户更新余额受影响行数:"+acount);
        return  mcount;
    }


}
