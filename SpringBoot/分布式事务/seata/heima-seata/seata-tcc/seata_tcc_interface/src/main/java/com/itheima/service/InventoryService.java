package com.itheima.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface InventoryService {


    /**
     * 预扣减库存操作
     * @param businessActionContext
     * @param productId  商品id
     * @param count      商品数量
     * @return
     */
    @TwoPhaseBusinessAction(name="decreaseTccTryPhase",commitMethod = "commit",rollbackMethod = "rollback")
    boolean prepareDecrease(BusinessActionContext businessActionContext,
                            @BusinessActionContextParameter(paramName = "productId")String productId,
                            @BusinessActionContextParameter(paramName = "count")Integer count);


    /**
     * 扣减库存的提交补偿
     * @param businessActionContext
     * @return
     */
    public boolean commit(BusinessActionContext businessActionContext) ;

    /**
     * 扣减库存的回滚补偿
     * @param businessActionContext
     * @return
     */
    public boolean rollback(BusinessActionContext businessActionContext) ;
}