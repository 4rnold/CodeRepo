package com.heima.account.service;

import com.heima.modules.po.AuthenticationPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "hitch-storage-server", path = "/storage/authentication", contextId = "authentication")
public interface AuthenticationAPIService {


    /**
     * 新增订单
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public AuthenticationPO add(@RequestBody AuthenticationPO record);

    @RequestMapping("/update")
    public void update(@RequestBody AuthenticationPO record);


    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<AuthenticationPO> selectlist(@RequestBody AuthenticationPO record);


    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public AuthenticationPO selectByID(@PathVariable("id") String id);

    /**
     * 根据手机号号码查询认证信息
     *
     * @param phone
     * @return
     */
    @RequestMapping("/selectByPhone/{phone}")
    public AuthenticationPO selectByPhone(@PathVariable("phone") String phone);


}