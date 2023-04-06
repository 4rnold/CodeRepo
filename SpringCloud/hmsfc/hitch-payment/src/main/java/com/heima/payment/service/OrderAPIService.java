package com.heima.payment.service;

import com.heima.modules.po.OrderPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "hitch-storage-server", path = "/storage/order", contextId = "order")
public interface OrderAPIService {

    /**
     * 发布行程
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public OrderPO add(@RequestBody OrderPO record);

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
     * 根据ID查看行程细节
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public OrderPO selectByID(@PathVariable("id") String id);

    /**
     * 根据行程ID查询订单
     * @param tripid
     * @return
     */
    @RequestMapping("/selectByTripid/{tripid}")
    public OrderPO selectByTripid(@PathVariable("tripid") String tripid);

}
