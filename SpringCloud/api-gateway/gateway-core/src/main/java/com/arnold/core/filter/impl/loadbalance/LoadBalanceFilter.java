package com.arnold.core.filter.impl.loadbalance;

import cn.hutool.core.lang.Assert;
import com.arnold.common.config.ServiceInstance;
import com.arnold.common.constants.FilterConst;
import com.arnold.common.rule.Rule;
import com.arnold.common.utils.AssertUtil;
import com.arnold.common.utils.JSONUtil;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;
import com.arnold.core.request.GatewayRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

@Slf4j
@FilterAspect(id = FilterConst.LOAD_BALANCE_FILTER_ID, name = FilterConst.LOAD_BALANCE_FILTER_NAME, order = FilterConst.LOAD_BALANCE_FILTER_ORDER)
public class LoadBalanceFilter implements Filter {
    @Override
    public void doFilter(GatewayContext context) {
        AssertUtil.notNull(context, "context is null");
        AssertUtil.notEmpty(context.getUniqueId(), "uniqueId is empty");
        AssertUtil.notNull(context.getRequest(), "request is null");

        //getLoadBalanceRule
        IGatewayLoadBalanceRule rule = getLoadBalanceRule(context);


        //get ServiceInstance base on Rule
        ServiceInstance chosenServiceInstance = rule.choose(context.getUniqueId(), context.isGray());

        //change request host
        GatewayRequest request = context.getRequest();
        String host = chosenServiceInstance.getIp() + ":" + chosenServiceInstance.getPort();
        log.info("loadBalanceFilter chosen host:{}", host);
        request.setModifyHost(host);
    }

    private IGatewayLoadBalanceRule getLoadBalanceRule(GatewayContext context) {
        Assert.notNull(context);
        Assert.notNull(context.getRule());
        //get filter config set
        Rule configRule = context.getRule();
        Set<Rule.FilterConfig> filterConfigSet = configRule.getFilterConfigSet();

        //get LoadBalanceFilterRule
        LoadbalanceRuleConfig loadbalanceRuleConfig = getLoadBalanceRuleConfigFormFilterConfig(filterConfigSet);

        IGatewayLoadBalanceRule loadBalanceRule = null;
        switch (loadbalanceRuleConfig.getLoadBalancerName()) {
            case FilterConst.LOAD_BALANCE_STRATEGY_RANDOM:
                loadBalanceRule = RandomLoadBalanceRule.getInstance();
                break;
            case FilterConst.LOAD_BALANCE_STRATEGY_ROUND_ROBIN:
                loadBalanceRule = RoundRobinLoadBalanceRule.getInstance(configRule.getServiceId());
                break;
            default:
                log.warn("no loadBalance rule available for :{}", loadbalanceRuleConfig.getLoadBalancerName());
                loadBalanceRule = RandomLoadBalanceRule.getInstance();
                break;
        }
        return loadBalanceRule;
    }

    private static LoadbalanceRuleConfig getLoadBalanceRuleConfigFormFilterConfig(Set<Rule.FilterConfig> filterConfigSet) {
        LoadbalanceRuleConfig loadbalanceConfigRule = new LoadbalanceRuleConfig();
        for (Rule.FilterConfig filterConfig : filterConfigSet) {
            if (filterConfig == null) {
                continue;
            }

            String filterConfigId = filterConfig.getId();
            if (FilterConst.LOAD_BALANCE_FILTER_ID.equals(filterConfigId)) {
                String config = filterConfig.getConfigContent();
                if (StringUtils.isNotEmpty(config)) {
                    loadbalanceConfigRule = JSONUtil.parse(config, LoadbalanceRuleConfig.class);
                }
                return loadbalanceConfigRule;
            }
        }
        return loadbalanceConfigRule;
    }

}
