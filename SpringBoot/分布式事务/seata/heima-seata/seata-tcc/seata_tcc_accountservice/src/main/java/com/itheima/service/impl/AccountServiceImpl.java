package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.AccountDao;
import com.itheima.service.AccountService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


@Service(timeout = 100000,retries = -1)
public class AccountServiceImpl implements AccountService {



    @Autowired
    private AccountDao accountDao;



    @Override
    public boolean preparePayment(BusinessActionContext businessActionContext,String userId, BigDecimal amount) {
        int res = accountDao.update(userId,amount);
        String xid = RootContext.getXID();
        System.out.println("account "+xid);
        return res > 0 ? Boolean.TRUE:Boolean.FALSE;
    }

    /**
     * 提交补偿
     * @param businessActionContext
     * @return
     */
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        System.out.println("============dubbo tcc 执行确认付款接口===============");
        String userId = (String)businessActionContext.getActionContext("userId");
        Integer amount = (Integer) businessActionContext.getActionContext("amount");
        accountDao.confirm(userId,new BigDecimal(amount));
        return Boolean.TRUE;
    }


    /**
     * 回滚补偿
     * @param businessActionContext
     * @return
     */
    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        System.out.println("============ dubbo tcc 执行取消付款接口===============");
        String userId = (String)businessActionContext.getActionContext("userId");
        Integer amount = (Integer)businessActionContext.getActionContext("amount");
        accountDao.cancel(userId,new BigDecimal(amount));
        return Boolean.TRUE;
    }
}
