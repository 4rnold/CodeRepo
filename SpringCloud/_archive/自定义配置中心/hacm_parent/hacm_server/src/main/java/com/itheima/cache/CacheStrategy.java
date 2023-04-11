package com.itheima.cache;

import com.itheima.domain.ConfigInfo;

/**
 * 定义缓存策略接口
 * 目的是：日后有新的缓存技术引入，可以直接实现此接口。便于扩展
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public interface CacheStrategy {

    /**
     * 存入缓存的方法
     * @param configInfo
     */
    void saveCache(ConfigInfo configInfo);

    /**
     * 根据key移除缓存
     * @param key
     */
    void removeCache(String key);

    /**
     * 根据key查找缓存
     * @param key
     * @return
     */
    ConfigInfo findCache(String key);
}
