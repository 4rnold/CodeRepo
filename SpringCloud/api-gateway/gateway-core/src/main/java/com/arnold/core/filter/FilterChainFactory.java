package com.arnold.core.filter;

import com.arnold.core.context.GatewayContext;

public interface FilterChainFactory {

    GatewayFilterChain buildFilterChain(GatewayContext context);
}
