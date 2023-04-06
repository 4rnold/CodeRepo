package com.heima.storage.service;

import com.heima.modules.po.PaymentPO;
import com.heima.storage.mapper.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentAPIService {

    @Autowired
    private PaymentMapper paymentMapper;

    /**
     * 新增订单
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public PaymentPO add(@RequestBody PaymentPO record) {
        PaymentPO existPO = selectByOrderId(record.getOrderId());
        if (null == existPO) {
            paymentMapper.insert(record);
        }
        return record;
    }

    @RequestMapping("/update")
    public void update(@RequestBody PaymentPO record) {
        paymentMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<PaymentPO> selectlist(@RequestBody PaymentPO record) {
        return paymentMapper.selectList(record);
    }


    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public PaymentPO selectByID(@PathVariable("id") String id) {
        return paymentMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByOrderId/{id}")
    public PaymentPO selectByOrderId(@PathVariable("id") String id) {
        return paymentMapper.selectByOrderId(id);
    }


}
