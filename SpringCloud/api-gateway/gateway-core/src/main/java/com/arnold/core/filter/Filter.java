package com.arnold.core.filter;

import com.arnold.core.context.GatewayContext;

public interface Filter {

    void doFilter(GatewayContext context);

    default int getOrder(){
        FilterAspect annotation = this.getClass().getAnnotation(FilterAspect.class);
        if (annotation != null){
            return annotation.order();
        }
        return Integer.MAX_VALUE;
    }
}
