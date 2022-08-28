package jiagoubaiduren.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.Lists;
import jiagoubaiduren.config.algorithm.OrderDbComplexKeysShardingAlgorithm;
import jiagoubaiduren.config.algorithm.OrderTbComplexKeysShardingAlgorithm;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

@MapperScan(sqlSessionFactoryRef = "sqlSessionFactoryStoreOrder", basePackages = {"jiagoubaiduren.mapper.storeorder"})
@Configuration
public class StoreOrderMybatisAutoConfiguration {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final String[] MAPPER_XML_PATH = new String[] {"classpath*:storeordermapper/*.xml"};


    /*** 店铺订单库 ***/

    @Bean(name = "dataSourceStoreOrder0")
    @ConfigurationProperties("spring.datasource.store-order0")
    public DataSource dataSourceStoreOrder0(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceStoreOrderSlave0")
    @ConfigurationProperties("spring.datasource.store-order0-slave")
    public DataSource dataSourceStoreOrderSlave0(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceStoreOrder1")
    @ConfigurationProperties("spring.datasource.store-order1")
    public DataSource dataSourceStoreOrder1(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceStoreOrderSlave1")
    @ConfigurationProperties("spring.datasource.store-order1-slave")
    public DataSource dataSourceStoreOrderSlave1(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactoryStoreOrder")
    public SqlSessionFactory sqlSessionFactoryOrder() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(getDataSource());
        factory.setVfs(SpringBootVFS.class);
        factory.setMapperLocations(resolveMapperLocations());
        return factory.getObject();
    }

    @Bean(name = "sqlSessionTemplateStoreOrder")
    public SqlSessionTemplate sqlSessionTemplateOrder() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactoryOrder());
    }

    public Resource[] resolveMapperLocations() {
        return Stream.of(Optional.ofNullable(MAPPER_XML_PATH).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }


    DataSource getDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("t_order");
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration("storeId", new OrderDbComplexKeysShardingAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("storeId", new OrderTbComplexKeysShardingAlgorithm()));
        shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfigurations());
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }


    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_order", "store_order_ds_master_slave_${0..1}.t_order_${0..3}");
        return result;
    }


    List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration("store_order_ds_master_slave_0", "dataSourceStoreOrder0", Arrays.asList("dataSourceStoreOrderSlave0"));
        MasterSlaveRuleConfiguration masterSlaveRuleConfig2 = new MasterSlaveRuleConfiguration("store_order_ds_master_slave_1", "dataSourceStoreOrder1", Arrays.asList("dataSourceStoreOrderSlave1"));
        return Lists.newArrayList(masterSlaveRuleConfig1, masterSlaveRuleConfig2);
    }

    Map<String, DataSource> createDataSourceMap() {
        final Map<String, DataSource> result = new HashMap<>();
        result.put("dataSourceStoreOrder0", dataSourceStoreOrder0());
        result.put("dataSourceStoreOrderSlave0", dataSourceStoreOrderSlave0());
        result.put("dataSourceStoreOrder1", dataSourceStoreOrder1());
        result.put("dataSourceStoreOrderSlave1", dataSourceStoreOrderSlave1());
        return result;
    }
}
