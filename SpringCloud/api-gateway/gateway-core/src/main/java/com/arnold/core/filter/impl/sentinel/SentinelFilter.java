package com.arnold.core.filter.impl.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.arnold.common.constants.FilterConst;
import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.ResponseException;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;

@FilterAspect(id = "SentinelFilterId", name = "SentinelFilterName", order = 0)
public class SentinelFilter implements Filter {

    public SentinelFilter() {

    }

    @Override
    public void doFilter(GatewayContext context) {
        Entry entry = null;
        ContextUtil.enter("ApiGateway", "SentinelFilter");
        try {
            entry = SphU.entry("FilterSphU");

            return;
        } catch (BlockException e) {
            throw new ResponseException(ResponseCode.SENTINEL_FAIL);
        }finally {
            entry.exit();
            ContextUtil.exit();
        }
    }
}
