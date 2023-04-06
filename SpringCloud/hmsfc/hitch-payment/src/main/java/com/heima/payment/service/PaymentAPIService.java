package com.heima.payment.service;

import com.heima.modules.po.PaymentPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "hitch-storage-server", path = "/storage/payment", contextId = "payment")
public interface PaymentAPIService {

    /**
     * 新增订单
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public PaymentPO add(@RequestBody PaymentPO record);

    @RequestMapping("/update")
    public void update(@RequestBody PaymentPO record);


    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<PaymentPO> selectlist(@RequestBody PaymentPO record);


    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public PaymentPO selectByID(@PathVariable("id") String id);

    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByOrderId/{id}")
    public PaymentPO selectByOrderId(@PathVariable("id") String id);


}
