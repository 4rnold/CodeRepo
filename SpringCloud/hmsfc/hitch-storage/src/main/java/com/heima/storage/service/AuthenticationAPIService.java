package com.heima.storage.service;

import com.heima.modules.po.AuthenticationPO;
import com.heima.storage.mapper.AuthenticationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authentication")
public class AuthenticationAPIService {
    @Autowired
    private AuthenticationMapper authenticationMapper;

    /**
     * 新增订单
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public AuthenticationPO add(@RequestBody AuthenticationPO record) {
        authenticationMapper.insert(record);
        return record;
    }

    @RequestMapping("/update")
    public void update(@RequestBody AuthenticationPO record) {
        authenticationMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<AuthenticationPO> selectlist(@RequestBody AuthenticationPO record) {
        return authenticationMapper.selectList(record);
    }


    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public AuthenticationPO selectByID(@PathVariable("id") String id) {
        return authenticationMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据手机号号码查询认证信息
     *
     * @param phone
     * @return
     */
    @RequestMapping("/selectByPhone/{phone}")
    public AuthenticationPO selectByPhone(@PathVariable("phone") String phone){
        return authenticationMapper.selectByPhone(phone);
    }
}
