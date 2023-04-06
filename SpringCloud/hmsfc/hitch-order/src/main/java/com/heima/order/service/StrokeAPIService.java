package com.heima.order.service;

import com.heima.modules.po.StrokePO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "hitch-storage-server", path = "/storage/stroke", contextId = "stroke")
public interface StrokeAPIService {


    /**
     * 根据ID查看行程细节
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public StrokePO selectByID(@PathVariable("id") String id);

}
