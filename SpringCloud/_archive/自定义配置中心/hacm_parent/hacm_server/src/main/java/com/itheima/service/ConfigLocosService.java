package com.itheima.service;

import com.itheima.dao.ConfigLocosDao;
import com.itheima.domain.ConfigInfo;
import com.itheima.domain.ConfigLocos;
import com.itheima.utils.IdWorkerUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 记录轨迹的业务层
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
public class ConfigLocosService {

    @Autowired
    private ConfigLocosDao configLocosDao;

    @Autowired
    private IdWorkerUtil idWorkerUtil;

    /**
     * 保存轨迹（它是从配置信息对象中获取数据）
     * @param configInfo
     */
    public void save(ConfigInfo configInfo){
        //1.创建轨迹对象
        ConfigLocos configLocos = new ConfigLocos();
        //2.拷贝数据
        BeanUtils.copyProperties(configInfo,configLocos,new String[]{"id","createTime"});
        //3.补全数据
        configLocos.setConfigInfoId(configInfo.getId());
        configLocos.setCreateTime(System.currentTimeMillis());
        configLocos.setId(String.valueOf(idWorkerUtil.nextId()));
        //4.保存
        configLocosDao.save(configLocos);
    }

    /**
     * 分页查询轨迹信息
     * @param configInfoId
     * @param page
     * @param size
     * @return
     */
    public Page<ConfigLocos> findPage(String configInfoId,int page,int size){
        //1.创建分页对象
        Pageable pageable = PageRequest.of(page-1,size);
        //2.分页查询
        Page<ConfigLocos> configLocosPage = configLocosDao.findByConfigInfoId(configInfoId,pageable);
        //3.返回
        return configLocosPage;
    }
}
