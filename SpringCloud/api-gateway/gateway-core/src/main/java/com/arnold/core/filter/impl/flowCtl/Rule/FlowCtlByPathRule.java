package com.arnold.core.filter.impl.flowCtl.Rule;

import com.arnold.common.constants.BasicConst;
import com.arnold.common.constants.FilterConst;
import com.arnold.common.rule.Rule;
import com.arnold.core.filter.impl.flowCtl.Rule.CountLimiter.CountLimiter;
import com.arnold.core.filter.impl.flowCtl.Rule.CountLimiter.GuavaCountLimiter;
import com.arnold.core.filter.impl.flowCtl.Rule.CountLimiter.RedisCountLimiter;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

public class FlowCtlByPathRule implements IGatewayFlowCtlRule {

    private String serviceId;

    private String path;
    Rule.FlowCtlConfig flowCtlConfig;

    private CountLimiter countLimiter;

    private static ConcurrentHashMap<String, FlowCtlByPathRule> flowCtlByPathRuleMap = new ConcurrentHashMap<>();

    public FlowCtlByPathRule(String serviceId, String path, Rule.FlowCtlConfig flowCtlConfig, CountLimiter countLimiter) {
        this.serviceId = serviceId;
        this.path = path;
        this.flowCtlConfig = flowCtlConfig;
        this.countLimiter = countLimiter;
    }
    public static FlowCtlByPathRule getInstance(String serviceId, String path, Rule.FlowCtlConfig flowCtlConfig){
        String key = StringUtils.join(serviceId, BasicConst.DIT_SEPARATOR, path);
        FlowCtlByPathRule flowCtlByPathRule = flowCtlByPathRuleMap.get(key);
        if (flowCtlByPathRule == null) {
            //判断countLimiter
            CountLimiter countLimiter = null;
            if (FilterConst.FLOW_CTL_MODEL_DISTRIBUTED.equalsIgnoreCase(flowCtlConfig.getModel())) {
                countLimiter = new RedisCountLimiter();
            } else {
                countLimiter = new GuavaCountLimiter();
            }
            flowCtlByPathRule = new FlowCtlByPathRule(serviceId, path,flowCtlConfig,countLimiter);
            flowCtlByPathRuleMap.putIfAbsent(key, flowCtlByPathRule);
        }


        return flowCtlByPathRule;
    }

    @Override
    public boolean doFlowCtlFilter() {
        if (flowCtlConfig == null || StringUtils.isEmpty(serviceId) ) {
            return true;
        }
        boolean flag = false;
        String key = StringUtils.join(serviceId, BasicConst.DIT_SEPARATOR, path);
        return countLimiter.doFlowCtl(key, flowCtlConfig.getLimitCount(), flowCtlConfig.getLimitDuration());

    }
}
