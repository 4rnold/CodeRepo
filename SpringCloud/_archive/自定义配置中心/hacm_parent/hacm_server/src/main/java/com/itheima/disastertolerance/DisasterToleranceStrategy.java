package com.itheima.disastertolerance;

import com.itheima.domain.ConfigInfo;

/**
 * 容灾机制的策略接口
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface DisasterToleranceStrategy {

    /**
     * 存入dfs
     * @param configInfo
     */
    void saveDfs(ConfigInfo configInfo);

    /**
     * 从dfs中移除
     * @param id
     */
    void removeDfs(String id);

    /**
     * 根据id从dfs中获取配置
     * @param id
     * @return
     */
    ConfigInfo findDfs(String id);
}
