package com.itheima.account.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.AccountDao;
import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

/***
 *
 *
 ****/
@Service(timeout = 100000,retries = -1)
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    /***
     * 修改账户余额
     * @param account
     * @return
     */
    @Override
    public int update(Account account){
        return accountDao.update(account);
    }

}
