package com.arnold.core;

import com.arnold.common.config.DynamicConfigManager;
import com.arnold.common.config.ServiceDefinition;
import com.arnold.common.config.ServiceInstance;
import com.arnold.common.utils.JSONUtil;
import com.arnold.common.utils.NetUtils;
import com.arnold.common.utils.TimeUtil;
import com.arnold.gateway.register.center.api.RegisterCenter;
import com.arnold.gateway.register.center.api.RegisterCenterListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

import static com.arnold.common.constants.BasicConst.COLON_SEPARATOR;

/**
 * Hello world!
 *
 */
@Slf4j
public class Bootstrap
{
    public static void main( String[] args )
    {
        //加载网关核心静态配置
        Config config = ConfigLoader.getInstance().load(args);
        System.out.println(config.getPort());

        //插件初始化
        //配置中心管理器初始化，连接配置中心，监听配置的新增、修改、删除
        //启动容器

        Container container = new Container(config);
        container.start();


        //连接注册中心，将注册中心的实例加载到本地

        ServiceDefinition serviceDefinition = buildGatewayServiceDefinition(config);
        ServiceInstance serviceInstance = buildGatewayServiceInstance(config);


        RegisterCenter registerCenter = registerAndSubscribe(config, serviceDefinition, serviceInstance);

        //服务优雅关闭
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                registerCenter.deregister(serviceDefinition,serviceInstance);
            }
        });

    }

    private static RegisterCenter registerAndSubscribe(Config config, ServiceDefinition serviceDefinition, ServiceInstance serviceInstance) {
        RegisterCenter registerCenter = null;

        registerCenter.register(serviceDefinition, serviceInstance);

        registerCenter.subscribeAllServices(new RegisterCenterListener() {
            @Override
            public void onChange(ServiceDefinition serviceDefinition, Set<ServiceInstance> serviceInstanceSet) {
                log.info("refresh service instance:{} {}",serviceDefinition, JSONUtil.toJSONString(serviceInstanceSet));
                DynamicConfigManager manager = DynamicConfigManager.getInstance();
                manager.addServiceInstance(serviceInstance.getUniqueId(), serviceInstanceSet);
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
