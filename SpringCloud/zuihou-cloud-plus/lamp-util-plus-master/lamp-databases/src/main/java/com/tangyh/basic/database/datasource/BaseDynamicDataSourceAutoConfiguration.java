package com.tangyh.basic.database.datasource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.tangyh.basic.database.dynamic.processor.DsThreadProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 动态数据源核心自动配置类
 * lamp.database.multiTenantType=DATASOURCE时， 子类需要继承它，并让程序启动时加载
 * <p>
 * 参考：https://github.com/baomidou/dynamic-datasource-spring-boot-starter/wiki/Debug-SourceCode
 *
 * @author TaoYu Kanyuxia
 * @author zuihou
 * @since 1.0.0
 */
@Slf4j
public abstract class BaseDynamicDataSourceAutoConfiguration {

    protected final DynamicDataSourceProperties properties;

    public BaseDynamicDataSourceAutoConfiguration(DynamicDataSourceProperties properties) {
        this.properties = properties;
        log.info("检测到 lamp.database.multiTenantType=DATASOURCE，已启用 数据源模式");
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new YmlDynamicDataSourceProvider(datasourceMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor(properties.isAllowedPublicOnly(), dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }

    /**
     * DATASOURCE 模式 自定义数据源处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        // 当前线程 获取数据源
        DsThreadProcessor threadProcessor = new DsThreadProcessor();
        // 当前session 获取数据源
        DsSessionProcessor sessionProcessor = new DsSessionProcessor();
        // sp el 表达式 获取数据源
        DsSpelExpressionProcessor dsSpelExpressionProcessor = new DsSpelExpressionProcessor();
        threadProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(dsSpelExpressionProcessor);
        return threadProcessor;
    }

}
