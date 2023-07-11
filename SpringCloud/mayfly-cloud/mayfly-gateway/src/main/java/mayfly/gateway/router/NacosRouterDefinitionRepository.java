package mayfly.gateway.router;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import mayfly.core.util.JsonUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * nacos动态路由定义
 *
 * @author meilin.huang
 * @date 2022-03-18 10:07
 */
@Slf4j
public class NacosRouterDefinitionRepository implements RouteDefinitionRepository {

    private final Map<String, RouteDefinition> routes = new HashMap<>();

    public NacosRouterDefinitionRepository(ApplicationEventPublisher publisher, ConfigService configService,
                                           String dataId, String group) {
        try {
            setRouteDefinitions(configService.getConfig(dataId, group, 5000));
        } catch (Exception e) {
            log.error("nacos加载动态路由解析失败", e);
        }

        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("监听获取到最新动态路由配置信息:\n {}", configInfo);
                    routes.clear();
                    setRouteDefinitions(configInfo);
                    // 发布刷新路由事件
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }

            });
        } catch (NacosException e) {
            log.error("注册nacos监听器失败", e);
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    /**
     * 加载路由
     */
    private void setRouteDefinitions(String content) {
        try {
            List<RouteDefinition> routeDefinitions = JsonUtils.parseList(content, RouteDefinition.class);
            routeDefinitions.forEach(routeDefinition -> {
                routes.put(routeDefinition.getId(), routeDefinition);
            });
        } catch (Exception e) {
            log.error("解析动态路由解析失败", e);
        }
    }
}
