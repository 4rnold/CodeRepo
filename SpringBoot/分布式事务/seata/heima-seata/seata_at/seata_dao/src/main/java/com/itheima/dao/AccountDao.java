package com.itheima.dao;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.Update;

/***
 *
 *
 ****/
public interface AccountDao {

    /****
     * 修改账户余额
     * @param account
     * @return
     */
    @Update("update account set money=money-#{money} where usernumber=#{usernumber}")
    int update(Account account);
}
