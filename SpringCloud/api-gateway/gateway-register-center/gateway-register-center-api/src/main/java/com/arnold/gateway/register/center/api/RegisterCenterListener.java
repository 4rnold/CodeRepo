/**
 * @projectName JianGateWay
 * @package tech.songjian.gateway.register.center.api
 * @className tech.songjian.gateway.register.center.com.arnold.gateway.register.center.api.RegisterCenterListener
 */
package com.arnold.gateway.register.center.api;

import com.arnold.common.config.ServiceDefinition;
import com.arnold.common.config.ServiceInstance;

import java.util.Set;

/**
 * RegisterCenterListener
 * @description 监听器
 * @author SongJian
 * @date 2023/6/9 19:47
 * @version
 */
public interface RegisterCenterListener {

    /**
     * 发生变化后的逻辑
     * @param serviceDefinition
     * @param serviceInstanceSet
     */
    void onChange(ServiceDefinition serviceDefinition, Set<ServiceInstance> serviceInstanceSet);
}

