package com.arnold.core.filter.impl.monitor;

import com.alibaba.nacos.client.naming.utils.RandomUtils;
import com.arnold.core.ConfigLoader;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.arnold.common.constants.FilterConst.*;

@Slf4j
@FilterAspect(id = MONITOR_END_FILTER_ID, name = MONITOR_END_FILTER_NAME, order = MONITOR_END_FILTER_ORDER)
public class MonitorEndFilter implements Filter {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    public MonitorEndFilter() {
        this.prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(
                    ConfigLoader.getConfig().getPrometheusPort()), 0);
            httpServer.createContext("/prometheus", exchange -> {
                String scrape = prometheusMeterRegistry.scrape();
                exchange.sendResponseHeaders(200, scrape.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(scrape.getBytes());
                }
            });
            new Thread(httpServer::start).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("prometheus started on port:{}", ConfigLoader.getConfig().getPrometheusPort());


        // mock
        Executors.newScheduledThreadPool(1000).scheduleAtFixedRate(() -> {
            Timer.Sample sample = Timer.start();
            try {
                Thread.sleep(RandomUtils.nextInt(100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Timer timer = prometheusMeterRegistry.timer("gateway_request",
                    "uniqueId", "backend-http-server:1.0.0",
                    "protocol", "http",
                    "path", "/http-server/ping" + RandomUtils.nextInt(10));
            sample.stop(timer);
        },200, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void doFilter(GatewayContext context) {
        Timer timer = prometheusMeterRegistry.timer("gateway_request",
                "uniqueId", context.getUniqueId(),
                "protocol", context.getProtocol(),
                "path", context.getRequest().getPath());
        context.getTimerSample().stop(timer);
    }
}