package com.itheima.service;

import com.itheima.commons.pojo.CommonEntity;

import java.util.Map;

/**
 * @Class: AnalysisService
 * @Package com.itheima.service
 * @Description: 分析服务接口
 * @Company: http://www.itheima.com/
 */
public interface AnalysisService {

    //指标聚合
    public Map<Object, Object> metricAgg(CommonEntity commonEntity) throws Exception;

    //获取搜索热词
    public Map<String, Long> hotWords(CommonEntity commonEntity) throws Exception;

}