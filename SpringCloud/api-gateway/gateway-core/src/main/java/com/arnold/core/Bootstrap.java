package com.arnold.core;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.arnold.common.config.DynamicServiceManager;
import com.arnold.common.config.ServiceDefinition;
import com.arnold.common.config.ServiceInstance;
import com.arnold.common.utils.JSONUtil;
import com.arnold.common.utils.NetUtils;
import com.arnold.common.utils.TimeUtil;
import com.arnold.core.redis.JedisUtil;
import com.arnold.gateway.config.center.api.ConfigCenter;
import com.arnold.gateway.register.center.api.RegisterCenter;
import com.arnold.gateway.register.center.api.RegisterCenterListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiOutput;

import java.util.*;

import static com.arnold.common.constants.BasicConst.COLON_SEPARATOR;

/**
 * Hello world!
 */
@Slf4j
public class Bootstrap {

    public static void main(String[] args) {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        JedisUtil.init("redis://192.168.138.6:6379/0");
        initDegradeRule();
        EventObserverRegistry.getInstance().addStateChangeObserver("http://172.25.144.1:8083/demo/http-demo/ping",
                (prevState, newState, rule, snapshotValue) -> {
                    if (newState == CircuitBreaker.State.OPEN) {
                        // 变换至 OPEN state 时会携带触发时的值
                        System.err.println(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue));
                    } else {
                        System.err.println(String.format("%s -> %s at %d", prevState.name(), newState.name(),
                                TimeUtil.currentTimeMillis()));
                    }
                });

        //加载网关核心静态配置
        Config config = ConfigLoader.getInstance().load(args);

        //插件初始化
        //配置中心管理器初始化，连接配置中心，监听配置的新增、修改、删除
//        ConfigCenter configCenter = null;
//        configCenter.subscribeRulesChange(rules -> DynamicConfigManager.getInstance().putAllRule(rules));

        configCenterInitAndSubscribe(config);

        //启动容器

        Container container = new Container(config);
        container.start();


        //连接注册中心，将注册中心的实例加载到本地
        //Service包含多个Instance
        ServiceDefinition serviceDefinition = buildGatewayServiceDefinition(config);
        ServiceInstance serviceInstance = buildGatewayServiceInstance(config);


        RegisterCenter registerCenter = registerAndSubscribe(config, serviceDefinition, serviceInstance);




        //服务优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                registerCenter.deregister(serviceDefinition, serviceInstance);
                container.shutdown();
            }
        });

    }


    private static void configCenterInitAndSubscribe(Config config) {
        ServiceLoader<ConfigCenter> serviceLoader = ServiceLoader.load(ConfigCenter.class);
        ConfigCenter configCenter = serviceLoader.findFirst().orElseThrow(() -> {
//            log.error("没有找到ConfigCenter的实现类");
            return new RuntimeException("没有找到ConfigCenter的实现类");
        });
        configCenter.init(config.getRegistryAddress(), config.getEnv());
        log.info("ConfigCenter初始化完成");
        configCenter.subscribeRulesChange(rules -> DynamicServiceManager.getInstance().putAllRule(rules));
        log.info("缓存rules成功");

    }

    private static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule("http://172.25.144.1:8083/demo/http-demo/ping")
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                // Max allowed response time
                .setCount(43)
                // Retry timeout (in second)
                .setTimeWindow(10)
                // Circuit breaker opens when slow request ratio > 60%
                .setSlowRatioThreshold(0.6)
                .setMinRequestAmount(100)
                .setStatIntervalMs(20000);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
        System.out.println("Degrade rule loaded: " + rules);
    }

    private static RegisterCenter registerAndSubscribe(Config config, ServiceDefinition serviceDefinition, ServiceInstance serviceInstance) {
        ServiceLoader<RegisterCenter> serviceLoader = ServiceLoader.load(RegisterCenter.class);
        RegisterCenter registerCenter = serviceLoader.findFirst().orElseThrow(() -> {
            log.error("没有找到【注册中心】的具体实现类！");
            return new RuntimeException("没有找到【注册中心】的具体实现类！");
        });

        registerCenter.init(config.getRegistryAddress(), config.getEnv());

        registerCenter.register(serviceDefinition, serviceInstance);

        registerCenter.subscribeAllServices(new RegisterCenterListener() {
            @Override
            public void onChange(ServiceDefinition serviceDefinition, Set<ServiceInstance> serviceInstanceSet) {
                log.info("refresh service instance:{} {}", serviceDefinition, JSONUtil.toJSONString(serviceInstanceSet));
                DynamicServiceManager manager = DynamicServiceManager.getInstance();
                manager.addServiceInstance(serviceDefinition.getUniqueId(), serviceInstanceSet);
                manager.putServiceDefinition(serviceDefinition.getUniqueId(), serviceDefinition);
            }
        });
        return registerCenter;
    }

    private static ServiceInstance buildGatewayServiceInstance(Config config) {
        String localIp = NetUtils.getLocalIp();
        int port = config.getPort();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setServiceInstanceId(localIp + COLON_SEPARATOR + port);
        serviceInstance.setIp(localIp);
        serviceInstance.setPort(port);
        serviceInstance.setRegisterTime(TimeUtil.currentTimeMillis());
        return serviceInstance;
    }


    private static ServiceDefinition buildGatewayServiceDefinition(Config config) {
        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setInvokerMap(Map.of());
        serviceDefinition.setUniqueId(config.getApplicationName());
        serviceDefinition.setServiceId(config.getApplicationName());
        serviceDefinition.setEnvType(config.getEnv());
        return serviceDefinition;
    }
}
