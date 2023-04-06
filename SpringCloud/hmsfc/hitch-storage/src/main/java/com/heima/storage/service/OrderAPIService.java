package com.heima.storage.service;

import com.heima.modules.po.OrderPO;
import com.heima.storage.mapper.OrderMapper;
import com.heima.storage.mapper.OrderShadowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderAPIService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderShadowMapper orderShadowMapper;

    /**
     * 新增订单
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public OrderPO add(@RequestBody OrderPO record) {
        orderMapper.insert(record);
        return record;
    }


    @RequestMapping("/addshadow")
    public OrderPO addShadow(@RequestBody OrderPO record) {
        orderShadowMapper.insert(record);
        return record;
    }

    @RequestMapping("/update")
    public void update(@RequestBody OrderPO record) {
        orderMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<OrderPO> selectlist(@RequestBody OrderPO record) {
        return orderMapper.selectList(record);
    }

    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectAvailableList")
    public List<OrderPO> selectAvailableList(@RequestBody OrderPO record) {
        return orderMapper.selectAvailableList(record);
    }

    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public OrderPO selectByID(@PathVariable("id") String id) {
        return orderMapper.selectByPrimaryKey(id);
    }


    @RequestMapping("/selectPaidList")
    List<OrderPO> selectPaidList(@RequestBody OrderPO orderPO) {
        return orderMapper.selectPaidList(orderPO);
    }

    /**
     * 根据行程ID查询订单
     *
     * @param tripid
     * @return
     */
    @RequestMapping("/selectByTripid/{tripid}")
    public OrderPO selectByTripid(@PathVariable("tripid") String tripid) {
        OrderPO orderPO = new OrderPO();
        orderPO.setPassengerStrokeId(tripid);
        List<OrderPO> orderPOList = selectlist(orderPO);
        if (null != orderPOList && !orderPOList.isEmpty()) {
            return orderPOList.get(0);
        }
        return null;
    }

}
