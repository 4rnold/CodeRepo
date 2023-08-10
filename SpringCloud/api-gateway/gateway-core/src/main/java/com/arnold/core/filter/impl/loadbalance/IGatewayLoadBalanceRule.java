package com.arnold.core.filter.impl.loadbalance;

import com.arnold.common.config.ServiceInstance;
import com.arnold.core.context.GatewayContext;

public interface IGatewayLoadBalanceRule {

    ServiceInstance choose(GatewayContext context);

    ServiceInstance choose(String serviceId, boolean gray);
}
