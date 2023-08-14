package com.arnold.core.filter;

import com.arnold.core.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GatewayFilterChain implements Filter {

    private List<Filter> filterList = new ArrayList<>();


    public GatewayFilterChain addFilter(Filter filter){
        filterList.add(filter);
        return this;
    }

    //addFilterList
    public GatewayFilterChain addFilterList(List<Filter> filterList){
        this.filterList.addAll(filterList);
        return this;
    }

    @Override
    public void doFilter(GatewayContext context) {
        if (filterList.size() > 0) {
            try {
                for (Filter filter : filterList) {
                    filter.doFilter(context);
                }
            } catch (Exception e) {
                log.error("doFilter error {}", e.getMessage());
                throw e;
            }
        }
    }

}
