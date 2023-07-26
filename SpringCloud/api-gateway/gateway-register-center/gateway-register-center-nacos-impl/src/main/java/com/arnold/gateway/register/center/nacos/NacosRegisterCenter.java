package com.arnold.gateway.register.center.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingMaintainFactory;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.common.executor.NameThreadFactory;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.arnold.common.config.ServiceDefinition;
import com.arnold.common.config.ServiceInstance;
import com.arnold.common.constants.GatewayConst;
import com.arnold.common.utils.JSONUtil;
import com.arnold.gateway.register.center.api.RegisterCenter;
import com.arnold.gateway.register.center.api.RegisterCenterListener;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class NacosRegisterCenter implements RegisterCenter {

    private String registerAddress;

    private String env;

    //服务实例信息
    private NamingService namingService;

    //服务定义信息
    private NamingMaintainService namingMaintainService;

    private List<RegisterCenterListener> registerCenterListenerList;


    @Override
    public void init(String regsiterAddress, String env) {
        this.registerAddress = regsiterAddress;
        this.env = env;

        try {
            this.namingMaintainService = NamingMaintainFactory.createMaintainService(registerAddress);
            this.namingService = NamingFactory.createNamingService(registerAddress);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance) {
        try {
            // 构造 nacos 服务实例信息
            // 因为我们要把自定义的信息转化成 nacos 能看懂的
            Instance nacosInstance = new Instance();
            nacosInstance.setInstanceId(serviceInstance.getServiceInstanceId());
            nacosInstance.setPort(serviceInstance.getPort());
            nacosInstance.setIp(serviceInstance.getIp());
            nacosInstance.setMetadata(Map.of(GatewayConst.META_DATA_KEY, JSONUtil.toJSONString(serviceInstance)));

            // 把服务实例注册到 nacos
            namingService.registerInstance(serviceDefinition.getServiceId(), env, nacosInstance);

            // 更新服务定义
            namingMaintainService.updateService(serviceDefinition.getServiceId(), env, 0,
                    Map.of(GatewayConst.META_DATA_KEY, JSONUtil.toJSONString(serviceDefinition)));

            log.info("【注册中心】注册完成： {} {}", serviceDefinition, serviceInstance);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregister(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance) {

    }

    @Override
    public void subscribeAllServices(RegisterCenterListener registerCenterListener) {
        // 把监听器添加到监听器列表中
        registerCenterListenerList.add(registerCenterListener);
        // 订阅所有（有些新注册的服务，gateway也要感知到）
        doSubscribeAllServices();
        // 可能有新服务加入，所以需要有一个定时任务来检查
        ScheduledExecutorService scheduledThreadPool = Executors
                .newScheduledThreadPool(1, new NameThreadFactory("doSubscribeAllServices"));
        scheduledThreadPool.scheduleWithFixedDelay(() -> doSubscribeAllServices(),
                10, 10, TimeUnit.SECONDS);

    }

    /**
     * 订阅逻辑实现：
     *      1、namingService 分页拉取当前所有服务
     *      2、遍历服务，为每一个服务添加事件监听器（这个事件监听器能够监听对应服务的变化）
     */
    private void doSubscribeAllServices() {
        try {
            // 已经订阅的服务
            Set<String> subscribeService = namingService.getSubscribeServices().stream()
                    .map(ServiceInfo::getName)
                    .collect(Collectors.toSet());

            int pageNo = 1;
            int pageSize = 100;

            // 分页从 nacos 拿到服务列表
            List<String> serviseList = namingService.getServicesOfServer(pageNo, pageSize, env).getData();

            while (CollectionUtils.isNotEmpty(serviseList)) {
                log.info("【注册中心】Nacos 现有服务列表大小为： {}", serviseList.size());

                // 遍历服务列表中的所有服务
                for (String service : serviseList) {
                    if (subscribeService.contains(service)) {
                        // 如果服务已经被订阅了，跳过
                        continue;
                    }
                    // 没有订阅的服务进行订阅
                    // nacos 事件监听器
                    EventListener eventListener = new NacosRegisterListener();
                    // 设置对该服务进行监听
                    eventListener.onEvent(new NamingEvent(service, null));
                    // namingService 用一个 ConcurrentSet 维护所有监听
                    namingService.subscribe(service, env, eventListener);

                    log.info("【注册中心】Nacos 成功订阅服务： {} {}", service, env);
                }
                // 继续分页获取服务列表
                serviseList = namingService.getServicesOfServer(++pageNo, pageSize, env).getData();
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 监听器，专门监听 NamingEvent
     *
     * 也就是服务实例变更的情况
     */
    public class NacosRegisterListener implements EventListener {

        @Override
        public void onEvent(Event event) {
            if (event instanceof NamingEvent) {

                NamingEvent namingEvent = (NamingEvent) event;
                String serviceName = namingEvent.getServiceName();

                try {
                    //获取服务定义信息
                    Service service = namingMaintainService.queryService(serviceName, env);
                    ServiceDefinition serviceDefinition = JSONUtil.parse(service.getMetadata()
                            .get(GatewayConst.META_DATA_KEY), ServiceDefinition.class);

                    //获取服务实例信息
                    List<Instance> allInstances = namingService.getAllInstances(service.getName(), env);
                    Set<ServiceInstance> set = new HashSet<>();

                    for (Instance instance : allInstances) {
                        ServiceInstance serviceInstance = JSONUtil.parse(instance.getMetadata()
                                .get(GatewayConst.META_DATA_KEY), ServiceInstance.class);
                        set.add(serviceInstance);
                    }
                    // 调用注册中心的监听器，将服务定义和服务实例进行缓存
                    registerCenterListenerList.stream().forEach(l -> l.onChange(serviceDefinition, set));
                    log.info("【注册中心】监听到 Nacos 事件，完成更新！");
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
