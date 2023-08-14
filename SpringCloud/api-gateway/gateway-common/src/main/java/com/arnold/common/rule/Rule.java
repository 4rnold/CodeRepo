package com.arnold.common.rule;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
public class Rule implements Comparable<Rule>, Serializable {
    private String id;
    private String name;
    private String protocol;
    //order
    private Integer order;
    private Set<FilterConfig> filterConfigSet = new HashSet<>();

    private String serviceId;

    private List<String> pathList;

    //这个prefix是根据 要请求的serviceId + 前缀判断
    private String prefix;

    private RetryConfig retryConfig;



    public Rule() {
    }

    public Rule(String id, String name, String protocol, Integer order,
                String serviceId,
                Set<FilterConfig> filterConfigSet) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.order = order;
        this.filterConfigSet = filterConfigSet;
        this.serviceId = serviceId;
    }

    //添加配置
    public void addFilterConfig(FilterConfig filterConfig){
        this.filterConfigSet.add(filterConfig);
    }

    //getFilterConfigById
    public FilterConfig getFilterConfigById(String id){
        for(FilterConfig filterConfig : filterConfigSet){
            if(filterConfig.getId().equalsIgnoreCase(id)){
                return filterConfig;
            }
        }
        return null;
    }


    @Override
    public int compareTo(Rule o) {
        if(order == null){
            return 0;
        }
        return order.compareTo(o.getOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id) && Objects.equals(name, rule.name) && Objects.equals(protocol, rule.protocol) && Objects.equals(order, rule.order) && Objects.equals(filterConfigSet, rule.filterConfigSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



    @Data
    public static class FilterConfig  {
        //filterId，根据FilterId找到Filter
        private String id;

        //config描述，每个filter自己的配置
        private String configContent;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilterConfig that = (FilterConfig) o;
            return Objects.equals(id, that.id) && Objects.equals(configContent, that.configContent);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    @Data
    public static class RetryConfig {
        private int retryTimes;
    }
}
