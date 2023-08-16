package com.arnold.core.filter;

import com.arnold.common.constants.FilterConst;
import com.arnold.common.rule.Rule;
import com.arnold.core.context.GatewayContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FilterChainFactoryImpl implements FilterChainFactory {

    //因为有共用缓存，所以需要创建一个单例
    private static final FilterChainFactory INSTANCE = new FilterChainFactoryImpl();

    public static FilterChainFactory getInstance(){
        return INSTANCE;
    }


    private Cache<String, GatewayFilterChain> chainCache = Caffeine.newBuilder()
            .recordStats().expireAfterWrite(10, TimeUnit.MINUTES).build();

    //map是为了多个filter的单例
    private Map<String, Filter> filterMap = new ConcurrentHashMap<>();

    private FilterChainFactoryImpl() {
        ServiceLoader<Filter> serviceLoader = ServiceLoader.load(Filter.class);
        for (Filter filter : serviceLoader) {
            FilterAspect annotation = filter.getClass().getAnnotation(FilterAspect.class);
            log.info("load filter {}", filter.getClass().getName());
            if (annotation != null) {
                String annotationId = annotation.id();
                if (StringUtils.isEmpty(annotationId)) {
                    annotationId = filter.getClass().getName();
                }
                filterMap.putIfAbsent(annotationId, filter);
            }
        }
    }

    @Override
    public GatewayFilterChain buildFilterChain(GatewayContext context) {
        return chainCache.get(context.getRule().getId(),
                k -> doBuildFilterChain(context.getRule()));
    }

    private GatewayFilterChain doBuildFilterChain(Rule rule) {
        GatewayFilterChain gatewayFilterChain = new GatewayFilterChain();
        ArrayList<Filter> filters = new ArrayList<>();
//        filters.add(filterMap.get(FilterConst.GRAY_FILTER_ID));
//        filters.add(filterMap.get(FilterConst.MONITOR_FILTER_ID));
//        filters.add(filterMap.get(FilterConst.MONITOR_END_FILTER_ID));
//        filters.add(filterMap.get(FilterConst.ROUTER_FILTER_ID));
        for (Map.Entry<String, Filter> filterEntry : filterMap.entrySet()) {
            filters.add(filterEntry.getValue());
        }

        addRuleFilters(rule, filters);
        CollectionUtils.filter(filters, PredicateUtils.notNullPredicate());

        filters.sort(Comparator.comparingInt(Filter::getOrder));

        gatewayFilterChain.addFilterList(filters);
        return gatewayFilterChain;
    }

    private void addRuleFilters(Rule rule, ArrayList<Filter> filters) {
        if (rule != null) {
            Set<Rule.FilterConfig> filterConfigSet = rule.getFilterConfigSet();
            for (Rule.FilterConfig filterConfig : filterConfigSet) {
                if (filterConfig == null) {
                    continue;
                }
                String filterId = filterConfig.getId();
                if (StringUtils.isNotEmpty(filterId) && filterMap.get(filterId) != null) {
                    filters.add(filterMap.get(filterId));
                }
            }
        }
    }
}
