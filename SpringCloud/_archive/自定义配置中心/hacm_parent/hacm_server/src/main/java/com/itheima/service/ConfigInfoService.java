package com.itheima.service;

import com.itheima.cache.CacheStrategy;
import com.itheima.dao.ConfigInfoDao;
import com.itheima.disastertolerance.DisasterToleranceStrategy;
import com.itheima.domain.ConfigInfo;
import com.itheima.domain.ConfigLocos;
import com.itheima.utils.IdWorkerUtil;
import com.itheima.utils.PropertiesUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 配置信息的业务层
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Service
public class ConfigInfoService {

    @Autowired
    private ConfigInfoDao configInfoDao;

    @Autowired
    private IdWorkerUtil idWorkerUtil;

    @Autowired
    private CacheStrategy cacheStrategy;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DisasterToleranceStrategy disasterToleranceStrategy;

    @Autowired
    private ConfigLocosService configLocosService;
    /**
     * 保存上传的配置信息
     * @param configInfo
     * @return
     */
    public String save(ConfigInfo configInfo){
        //1.生成id
        String id = String.valueOf(idWorkerUtil.nextId());
        //填充主键
        configInfo.setId(id);
        //2.保存
        configInfoDao.save(configInfo);
        //3.把ConfigInfo存入缓存
        cacheStrategy.saveCache(configInfo);
        //记录轨迹
        configLocosService.save(configInfo);
        //4.把操作和要操作的内容写入队列
        Map<String,Object> map = new HashMap<>();
        map.put("operated","change");
        map.put("data",configInfo);
        rabbitTemplate.convertAndSend("synchronizationdfs",map);
        //5.返回id
        return id;
    }

    /**
     * 创建配置信息对象
     */
    public void create(ConfigInfo configInfo){
        //生成ID
        String id = String.valueOf(idWorkerUtil.nextId());
        //1.补全信息
        configInfo.setId(id);
        configInfo.setConfigDetail("");
        configInfo.setCreateTime(new Date());
        configInfo.setUpdateTime(new Date());
        //2.调用持久层保存
        configInfoDao.save(configInfo);
    }


    /**
     * 根据Id查询配置信息对象
     * @param id
     * @return
     */
    public ConfigInfo findById(String id){
        //定义返回值
        ConfigInfo configInfo = null;
        try {
            //1.根据id前往缓存中查找
            configInfo = cacheStrategy.findCache(id);
            //2.判断缓存中是否存在
            if (configInfo != null) {
                //System.out.println("从缓存中获取的");
                return configInfo;
            }
            //3.不存在，去数据库中查找
            configInfo = configInfoDao.findById(id).get();
            //System.out.println("从数据库中获取的");
            //4.存入缓存中
            cacheStrategy.saveCache(configInfo);
        }catch (Exception e){
            //当缓存和关系型数据库获取时出现了异常，我们需要启动容灾
            configInfo = disasterToleranceStrategy.findDfs(id);
        }
        return configInfo;
    }

    /**
     * 删除配置信息对象
     * @param configInfo
     */
    public void removeAll(ConfigInfo configInfo){
        //1.删除持久化数据
        configInfoDao.delete(configInfo);
        //2.删除缓存
        cacheStrategy.removeCache(configInfo.getId());
        //3.把操作和要操作的内容写入队列
        Map<String,Object> map = new HashMap<>();
        map.put("operated","del");
        map.put("data",configInfo);
        rabbitTemplate.convertAndSend("synchronizationdfs",map);
    }


    /**
     * 添加配置项
     * @param id
     * @param configName
     * @param configValue
     */
    public void add(String id,String configName,String configValue){
        //1.根据id查询配置信息
        ConfigInfo configInfo = configInfoDao.getOne(id);
        //2.把配置信息的配置详情取出来
        String configDetail = configInfo.getConfigDetail();
        //3.判断配置详情是不是有数据
        Properties properties = null;
        if("".equals(configDetail)){
            //没有任何配置
            properties = new Properties();
            //存入数据
            properties.put(configName,configValue);
        }else {
            //已经有了配置项
            //先把configDetail转成Properties
            properties = PropertiesUtil.toProperties(configDetail);
            //把配置存入
            properties.put(configName,configValue);
        }
        //4.把properties转成json，并给configInfo的configDetail赋值
        String json = PropertiesUtil.toJson(properties);
        configInfo.setConfigDetail(json);
        //5.更新
        configInfoDao.save(configInfo);
        //6.移除缓存
        cacheStrategy.removeCache(configInfo.getId());
        //记录轨迹
        configLocosService.save(configInfo);
        //7.把操作和要操作的内容写入队列
        Map<String,Object> map = new HashMap<>();
        map.put("operated","change");
        map.put("data",configInfo);
        rabbitTemplate.convertAndSend("synchronizationdfs",map);
    }


    /**
     * 删除配置项
     * @param id
     * @param configName
     */
    public void remove(String id,String configName){
        //1.根据id查询配置项
        ConfigInfo configInfo = configInfoDao.getOne(id);
        //2.取出详情并转成properties
        String configDetail = configInfo.getConfigDetail();
        Properties properties = PropertiesUtil.toProperties(configDetail);
        //3.移除指定配置项
        properties.remove(configName);
        //4.转成Json
        String json = PropertiesUtil.toJson(properties);
        //5.给configInfo赋值
        configInfo.setConfigDetail(json);
        //6.更新
        configInfoDao.save(configInfo);
        //7.移除缓存
        cacheStrategy.removeCache(configInfo.getId());
        //记录轨迹
        configLocosService.save(configInfo);
        //8.把操作和要操作的内容写入队列
        Map<String,Object> map = new HashMap<>();
        map.put("operated","change");
        map.put("data",configInfo);
        rabbitTemplate.convertAndSend("synchronizationdfs",map);
    }

    /**
     * 更新配置信息
     * @param configInfo
     */
    public void update(ConfigInfo configInfo){
        //1.根据id查询出原有的对象
        ConfigInfo dbConfigInfo = configInfoDao.getOne(configInfo.getId());
        //2.使用参数的详情，给数据库的详情赋值
        dbConfigInfo.setConfigDetail(configInfo.getConfigDetail());
        //3.更新
        configInfoDao.save(dbConfigInfo);
        //4.移除缓存
        cacheStrategy.removeCache(configInfo.getId());
        //记录轨迹
        configLocosService.save(configInfo);
        //5.把操作和要操作的内容写入队列
        Map<String,Object> map = new HashMap<>();
        map.put("operated","change");
        map.put("data",configInfo);
        rabbitTemplate.convertAndSend("synchronizationdfs",map);
    }


    /**
     * 带有条件的查询所有
     * @param condition
     * @param page
     * @param size
     * @return
     */
    public Page findAll(ConfigInfo condition,int page,int size){
        //1.创建查询条件
        Specification<ConfigInfo> specification = this.generatedCondition(condition);
        //2.创建分页信息对象
        Pageable pageable = PageRequest.of(page-1,size);
        //3.使用条件对象和分页对象查询,并返回
        return configInfoDao.findAll(specification,pageable);
    }


    private Specification<ConfigInfo> generatedCondition(ConfigInfo condition){

        return new Specification<ConfigInfo>() {
            @Override
            public Predicate toPredicate(Root<ConfigInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //1.创建用于存放条件的集合对象
                List<Predicate> predicateList = new ArrayList<>();
                //2.判断条件
                if(!StringUtils.isEmpty(condition.getProjectGroup())) {
                    Predicate p1 = cb.equal(root.get("projectGroup"),condition.getProjectGroup());
                    predicateList.add(p1);
                }
                if(!StringUtils.isEmpty(condition.getProjectGroup())) {
                    Predicate p2 = cb.equal(root.get("projectName"),condition.getProjectName());
                    predicateList.add(p2);
                }
                if(!StringUtils.isEmpty(condition.getProjectGroup())) {
                    Predicate p3 = cb.equal(root.get("envName"),condition.getEnvName());
                    predicateList.add(p3);
                }
                if(!StringUtils.isEmpty(condition.getProjectGroup())) {
                    Predicate p4 = cb.equal(root.get("clusterNumber"),condition.getClusterNumber());
                    predicateList.add(p4);
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
