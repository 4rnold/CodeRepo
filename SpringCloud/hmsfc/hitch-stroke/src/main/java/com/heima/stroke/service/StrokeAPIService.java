package com.heima.stroke.service;

import com.heima.modules.po.AccountPO;
import com.heima.modules.po.StrokePO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "hitch-storage-server", path = "/storage/stroke",contextId = "stroke")
public interface StrokeAPIService {
    /**
     * 发布行程
     *
     * @param strokePO
     * @return
     */
    @RequestMapping("/publish")
    public StrokePO publish(@RequestBody StrokePO strokePO);

    /**
     * 修改行程
     *
     * @param strokePO
     */

    @RequestMapping("/update")
    public void update(@RequestBody StrokePO strokePO);


    /**
     * 查询行程列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<StrokePO> selectlist(@RequestBody StrokePO record);


    /**
     * 根据ID查看行程细节
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public StrokePO selectByID(@PathVariable("id") String id);

}
