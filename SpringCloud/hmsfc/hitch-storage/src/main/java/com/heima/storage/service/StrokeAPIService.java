package com.heima.storage.service;

import com.heima.modules.po.StrokePO;
import com.heima.storage.mapper.StrokeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stroke")
public class StrokeAPIService {

    @Autowired
    private StrokeMapper strokeMapper;

    /**
     * 发布行程
     *
     * @param strokePO
     * @return
     */
    @RequestMapping("/publish")
    public StrokePO publish(@RequestBody StrokePO strokePO) {
        strokeMapper.insert(strokePO);
        return strokePO;
    }

    @RequestMapping("/update")
    @CacheEvict(cacheNames = "com.heima.modules.po.StrokePO", key = "#strokePO.id")
    public void update(@RequestBody StrokePO strokePO) {
        strokeMapper.updateByPrimaryKeySelective(strokePO);
    }


    /**
     * 查询行程列表
     *
     * @param record
     * @return
     */
    @RequestMapping("/selectlist")
    public List<StrokePO> selectlist(@RequestBody StrokePO record) {
        return strokeMapper.selectList(record);
    }




    /**
     * 根据ID查看行程细节
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectByID/{id}")
    @Cacheable(cacheNames = "com.heima.modules.po.StrokePO", key = "#id")
    public StrokePO select(@PathVariable("id") String id) {
        return strokeMapper.selectByPrimaryKey(id);
    }

}
