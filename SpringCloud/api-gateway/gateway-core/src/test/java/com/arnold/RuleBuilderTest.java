package com.arnold;

import cn.hutool.json.JSONObject;
import com.arnold.common.constants.FilterConst;
import com.arnold.common.rule.Rule;
import com.arnold.common.utils.JSONUtil;
import com.arnold.core.filter.impl.loadbalance.LoadbalanceRuleConfig;
import com.arnold.gateway.config.center.ConfigInfo;
import org.apache.commons.collections4.SetUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuleBuilderTest {

    public static void main(String[] args) {

        Rule rule = new Rule();
        rule.setName("demo");
        rule.setId("1");
        rule.setServiceId("backend-http-server");
        rule.setPrefix("/demo/");

        //重试
        Rule.RetryConfig retryConfig = new Rule.RetryConfig();
        retryConfig.setRetryTimes(3);
        rule.setRetryConfig(retryConfig);

        //filter配置
        //LoadBalanceFilter
        LoadbalanceRuleConfig loadbalanceRuleConfig = new LoadbalanceRuleConfig();
        loadbalanceRuleConfig.setLoadBalancerName(FilterConst.LOAD_BALANCE_STRATEGY_ROUND_ROBIN);
        String loadbalanceRuleConfigJsonStr = cn.hutool.json.JSONUtil.toJsonStr(loadbalanceRuleConfig);

        Rule.FilterConfig filterConfig = new Rule.FilterConfig();
        filterConfig.setId("load_balancer_filter");
        filterConfig.setConfigContent(loadbalanceRuleConfigJsonStr);

        Rule.FilterConfig userFilterConfig = new Rule.FilterConfig();
        userFilterConfig.setId("user_auth_filter");

        rule.addFilterConfig(filterConfig);
        rule.addFilterConfig(userFilterConfig);


        //Set<Rule.FlowCtlConfig> flowCtlConfigSet = rule.getFlowCtlConfigSet();
        Rule.FlowCtlConfig flowCtlConfig = new Rule.FlowCtlConfig();
        flowCtlConfig.setType("path");
        flowCtlConfig.setPath("/demo/http-demo/ping");
        flowCtlConfig.setModel("Singleton");
        flowCtlConfig.setLimitDuration(5000);
        flowCtlConfig.setLimitCount(1);
        HashSet<Rule.FlowCtlConfig> flowCtlConfigs = SetUtils.hashSet(flowCtlConfig);

        rule.setFlowCtlConfigSet(flowCtlConfigs);

        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setRules(List.of(rule));

        JSONObject entries = cn.hutool.json.JSONUtil.parseObj(configInfo, true);
        System.out.println(JSONUtil.toJSONString(entries));

    }
}
