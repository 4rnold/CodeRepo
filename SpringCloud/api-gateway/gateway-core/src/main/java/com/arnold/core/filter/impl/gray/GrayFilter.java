package com.arnold.core.filter.impl.gray;

import com.arnold.common.constants.FilterConst;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;
import lombok.extern.slf4j.Slf4j;

/**
 * GrayFilter设置gray，从DynamicServiceManager获取service时判断是否返回
 */
@Slf4j
@FilterAspect(id = FilterConst.GRAY_FILTER_ID, name = FilterConst.GRAY_FILTER_NAME, order = FilterConst.GRAY_FILTER_ORDER)
public class GrayFilter implements Filter {
    @Override
    public void doFilter(GatewayContext context) {

        //head中的gray字段，强制返回gray service
        String gray = context.getRequest().getHttpHeaders().get("gray");
        if (gray != null && gray.equals("true")) {
            context.setGray(true);
        }

        //
    }
}
