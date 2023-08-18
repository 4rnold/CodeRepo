package com.arnold.core.filter.impl.monitor;

import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import static com.arnold.common.constants.FilterConst.*;

@Slf4j
@FilterAspect(id = MONITOR_FILTER_ID, name = MONITOR_FILTER_NAME, order = MONITOR_FILTER_ORDER)
public class MonitorFilter implements Filter {
    @Override
    public void doFilter(GatewayContext ctx) {
        ctx.setTimerSample(Timer.start());
    }
}
