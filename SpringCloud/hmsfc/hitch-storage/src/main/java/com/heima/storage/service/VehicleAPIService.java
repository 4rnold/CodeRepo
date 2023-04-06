package com.heima.storage.service;

import com.heima.modules.po.VehiclePO;
import com.heima.storage.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleAPIService {

    @Autowired
    private VehicleMapper vehicleMapper;

    /**
     * 新增订单
     *
     * @param record
     * @return
     */
    @RequestMapping("/add")
    public VehiclePO add(@RequestBody VehiclePO record) {
        vehicleMapper.insert(record);
        return record;
    }

    @RequestMapping("/update")
    public void update(@RequestBody VehiclePO record) {
        vehicleMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 查询订单列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<VehiclePO> selectlist(@RequestBody VehiclePO record) {
        return vehicleMapper.selectList(record);
    }


    /**
     * 根据ID查看订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    public VehiclePO select(@PathVariable("id") String id) {
        return vehicleMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据手机号码查询用户信息
     *
     * @param phone
     * @return
     */
    @RequestMapping("/selectByPhone/{phone}")
    VehiclePO selectByPhone(@PathVariable("phone") String phone){
        return vehicleMapper.selectByPhone(phone);
    }
}
