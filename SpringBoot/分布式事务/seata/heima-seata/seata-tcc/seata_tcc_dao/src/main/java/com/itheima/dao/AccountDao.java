package com.itheima.dao;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface AccountDao {

    /**
     * 更新用户金额到冻结金额字段中
     * @param userId
     * @param amount
     * @return
     */
    @Update("update account set balance = balance - #{amount}," +
            " freeze_amount= freeze_amount + #{amount} ,update_time = now()" +
            " where user_id =#{userId}  and  balance > 0  ")
    int update(@Param("userId") String userId,@Param("amount") BigDecimal amount);


    /**
     * 把冻结资金清空（表示支付成功）
     * @param userId
     * @param amount
     * @return
     */
    @Update("update account set " +
            " freeze_amount= freeze_amount - #{amount}" +
            " where user_id =#{userId}  and freeze_amount >0 ")
    int confirm(@Param("userId") String userId,@Param("amount") BigDecimal amount);


    /**
     * 把用户冻结资金加回到可用资金（表示支付失败）
     * @param userId
     * @param amount
     * @return
     */
    @Update("update account set balance = balance + #{amount}," +
            " freeze_amount= freeze_amount -  #{amount} " +
            " where user_id =#{userId}  and freeze_amount >0")
    int cancel(@Param("userId") String userId,@Param("amount") BigDecimal amount);


    /**
     * 根据userId获取用户账户信息
     *
     * @param userId 用户id
     * @return AccountDO account do
     */
    @Select("select id,user_id,balance, freeze_amount from account where user_id =#{userId} limit 1")
    Account findByUserId(String userId);
}
