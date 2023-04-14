
package com.itheima.service;


import com.itheima.domain.Order;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.omg.PortableInterceptor.ObjectReferenceFactory;

import java.math.BigDecimal;
@LocalTCC
public interface OrderService {

    /**
     * 创建订单并且进行扣除账户余额支付，并进行库存扣减操作
     *
     * @return string string
     */
    @TwoPhaseBusinessAction(name = "orderPayTccTryPhase",commitMethod = "commit",rollbackMethod = "rollback")
    boolean prepareUpdateState(BusinessActionContext businessActionContext,@BusinessActionContextParameter(paramName = "orderId")String orderId);

    /**
     * 修改订单状态为支付完成
     * @param businessActionContext
     */
    public boolean commit(BusinessActionContext businessActionContext);

    /**
     * 修改订单状态为支付失败
     * @param businessActionContext
     */
    public boolean rollback(BusinessActionContext businessActionContext);


    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    Order findById(String id);
}
