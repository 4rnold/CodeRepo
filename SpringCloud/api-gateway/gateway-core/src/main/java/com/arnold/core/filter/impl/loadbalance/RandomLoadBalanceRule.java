package com.arnold.core.filter.impl.loadbalance;

import com.arnold.common.config.DynamicServiceManager;
import com.arnold.common.config.ServiceInstance;
import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.NotFoundException;
import com.arnold.core.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class RandomLoadBalanceRule implements IGatewayLoadBalanceRule{

    //单例
    private static final RandomLoadBalanceRule INSTANCE = new RandomLoadBalanceRule();

    public static RandomLoadBalanceRule getInstance(){
        return INSTANCE;
    }

    private RandomLoadBalanceRule(){

    }


    @Override
    public ServiceInstance choose(GatewayContext context) {
        String serviceUniqueId = context.getUniqueId();
        return choose(serviceUniqueId,context.isGray());
    }

    @Override
    public ServiceInstance choose(String serviceId, boolean gray) {
        Set<ServiceInstance> serviceInstances =
                DynamicServiceManager.getInstance().getServiceInstanceByUniqueId(serviceId, gray);
        if (CollectionUtils.isEmpty(serviceInstances)) {
            log.warn("no instance available for :{}", serviceId);
            throw new NotFoundException(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
        }
        ArrayList<ServiceInstance> inst = new ArrayList<>(serviceInstances);
        int index = ThreadLocalRandom.current().nextInt(inst.size());
        return inst.get(index);
    }
}
