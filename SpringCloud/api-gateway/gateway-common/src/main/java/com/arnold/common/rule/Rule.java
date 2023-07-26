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

    private String prefix;



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
        return Objects.hash(id, name, protocol, order, filterConfigSet);
    }

    @Data
    public static class FilterConfig  {
        //id
        private String id;
        //config
        private String config;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilterConfig that = (FilterConfig) o;
            return Objects.equals(id, that.id) && Objects.equals(config, that.config);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
