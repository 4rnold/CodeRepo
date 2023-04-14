package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.InventoryDao;
import com.itheima.service.InventoryService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;

@Service(timeout = 100000,retries = -1)
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryDao inventoryDao;


    /**
     * 预扣减库存操作
     * @param businessActionContext
     * @param productId  商品id
     * @param count      商品数量
     * @return
     */
    @Override
    public boolean prepareDecrease(BusinessActionContext businessActionContext,String productId,Integer count){
        int res = inventoryDao.decrease(productId,count);
        String xid = RootContext.getXID();
        System.out.println("inventory "+xid);
        return res > 0 ? Boolean.TRUE:Boolean.FALSE;
    }


    /**
     * 扣减库存的提交补偿
     * @param businessActionContext
     * @return
     */
    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        System.out.println("==========调用扣减库存confirm方法===========");
        String productId = (String) businessActionContext.getActionContext("productId");
        Integer count = (Integer)businessActionContext.getActionContext("count");
        inventoryDao.confirm(productId,count);
        return true;
    }

    /**
     * 扣减库存的回滚补偿
     * @param businessActionContext
     * @return
     */
    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        System.out.println("==========调用扣减库存取消方法===========");
        String productId = (String) businessActionContext.getActionContext("productId");
        Integer count = (Integer)businessActionContext.getActionContext("count");
        inventoryDao.cancel(productId,count);
        return true;
    }

}
