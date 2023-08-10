package com.arnold.core.filter.impl.loadbalance;

import com.arnold.common.constants.FilterConst;
import lombok.Data;

@Data
public class LoadbalanceRuleConfig {

    private String loadBalancerName = FilterConst.LOAD_BALANCE_STRATEGY_RANDOM;

}
