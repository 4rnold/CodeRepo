package com.heima.notice.service;

import com.heima.modules.po.StrokePO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
