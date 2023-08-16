package com.arnold.core.filter.impl.flowCtl;

import com.arnold.common.constants.FilterConst;
import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.ResponseException;
import com.arnold.common.rule.Rule;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;
import com.arnold.core.filter.impl.flowCtl.Rule.FlowCtlByPathRule;
import com.arnold.core.filter.impl.flowCtl.Rule.IGatewayFlowCtlRule;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@FilterAspect(id = FilterConst.FLOW_CTL_FILTER_ID, name = FilterConst.FLOW_CTL_FILTER_NAME, order = FilterConst.FLOW_CTL_FILTER_ORDER)
public class FlowCtlFilter implements Filter {

    @Override
    public void doFilter(GatewayContext context) {
        Rule rule = context.getRule();
        if (rule == null) {
            return;
        }

        //获取rule中的流控配置
        Set<Rule.FlowCtlConfig> flowCtlConfigSet = rule.getFlowCtlConfigSet();
        IGatewayFlowCtlRule flowCtlRule = null;

        //根据配置选择 flowCtlRule
        for (Rule.FlowCtlConfig flowCtlConfig : flowCtlConfigSet) {
            String requestPath = context.getRequest().getPath();

            //根据path限流，且path匹配
            if (flowCtlConfig.getType().equalsIgnoreCase(FilterConst.FLOW_CTL_TYPE_PATH) &&
                    requestPath.equals(flowCtlConfig.getPath())) {
                flowCtlRule = FlowCtlByPathRule.getInstance(rule.getServiceId(), requestPath, flowCtlConfig);
                boolean flowCtlResult = flowCtlRule.doFlowCtlFilter();
                if (!flowCtlResult) {
                    throw new ResponseException(ResponseCode.FLOWCTL_FAIL);
                }
            } else {
                //根据service限流
            }
        }
    }

}
