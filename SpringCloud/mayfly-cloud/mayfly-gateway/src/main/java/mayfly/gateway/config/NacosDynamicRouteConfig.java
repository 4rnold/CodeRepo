package mayfly.gateway.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import mayfly.gateway.router.NacosRouterDefinitionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author meilin.huang
 * @date 2022-03-18 11:08
 */
@Configuration
public class NacosDynamicRouteConfig {

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public String serverAddr;

    @Value("${nacos.gateway.route.config.data-id}")
    public String dataId;

    @Value("${nacos.gateway.route.config.group}")
    public String group;

    /**
     * 实例化Nacos配置服务
     */
    @Bean
    public ConfigService configService() throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, this.serverAddr);
        return NacosFactory.createConfigService(properties);
    }

    @Bean
    public NacosRouterDefinitionRepository nacosRouteDefinitionRepository(ApplicationEventPublisher publisher, ConfigService configService) {
        return new NacosRouterDefinitionRepository(publisher, configService, dataId, group);
    }
}
