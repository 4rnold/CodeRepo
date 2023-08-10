package com.arnold.core.filter.impl.loadbalance;

import cn.hutool.core.map.MapUtil;
import com.arnold.common.config.DynamicServiceManager;
import com.arnold.common.config.ServiceInstance;
import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.NotFoundException;
import com.arnold.core.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RoundRobinLoadBalanceRule implements IGatewayLoadBalanceRule {

    //每个Service请求的rule都是固定的，所以可以做缓存
    private static ConcurrentHashMap<String, RoundRobinLoadBalanceRule> serviceRuleMap = new ConcurrentHashMap<>();

    private AtomicInteger position = new AtomicInteger(1);
    public static RoundRobinLoadBalanceRule getInstance(String serviceId) {
        RoundRobinLoadBalanceRule rule = serviceRuleMap.get(serviceId);
        if (rule == null) {
            rule = new RoundRobinLoadBalanceRule();
            serviceRuleMap.put(serviceId, rule);
        }
        return rule;
    }

    @Override
    public ServiceInstance choose(GatewayContext context) {
        String serviceUniqueId = context.getUniqueId();
        return choose(serviceUniqueId, context.isGray());
    }

    @Override
    public ServiceInstance choose(String serviceId, boolean gray) {
        Set<ServiceInstance> serviceInstances = DynamicServiceManager.getInstance().getServiceInstanceByUniqueId(serviceId, gray);
        if (CollectionUtils.isEmpty(serviceInstances)) {
            log.warn("no instance available for :{}", serviceId);
            throw new NotFoundException(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
        }
        ArrayList<ServiceInstance> serviceInstancesList = new ArrayList<>(serviceInstances);
        int pos = Math.abs(this.position.incrementAndGet())%serviceInstancesList.size();
        return serviceInstancesList.get(pos);
    }
}
