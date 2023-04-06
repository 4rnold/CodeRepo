package com.heima.order.service;

import com.heima.modules.po.OrderPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "hitch-storage-server", path = "/storage/order")
public interface OrderAPIService {

    /**
     * 发布行程
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public OrderPO add(@RequestBody OrderPO record);

    /**
     * 添加订单影子表
     *
     * @param record
     * @return
     */
    @RequestMapping("/addshadow")
    public OrderPO addShadow(@RequestBody OrderPO record);

    /**
     * 更新数据
     *
     * @param record
     */
    @RequestMapping("/update")
    public void update(@RequestBody OrderPO record);

    /**
     * 查询行程列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<OrderPO> selectlist(@RequestBody OrderPO record);

    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectAvailableList")
    public List<OrderPO> selectAvailableList(@RequestBody OrderPO record);


    /**
     * 根据ID查看行程细节
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public OrderPO select(@PathVariable("id") String id);

    /**
     * 获取已支付的订单列表
     *
     * @param orderPO
     * @return
     */
    @RequestMapping("/selectPaidList")
    List<OrderPO> selectPaidList(@RequestBody OrderPO orderPO);
}
