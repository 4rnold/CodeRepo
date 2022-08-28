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
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
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

@MapperScan(sqlSessionFactoryRef = "sqlSessionFactoryOrder", basePackages = {"jiagoubaiduren.mapper.order"})
@Configuration
public class OrderMybatisAutoConfiguration {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final String[] MAPPER_XML_PATH = new String[] {"classpath*:ordermapper/*.xml"};

    /*** 买家订单库 ***/

    @Bean(name = "dataSourceOrder0")
    @Primary
    @ConfigurationProperties("spring.datasource.order0")
    public DataSource dataSourceOrder0(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceOrderSlave0")
    @ConfigurationProperties("spring.datasource.order0-slave")
    public DataSource dataSourceOrderSlave0(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceOrder1")
    @ConfigurationProperties("spring.datasource.order1")
    public DataSource dataSourceOrder1(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceOrderSlave1")
    @ConfigurationProperties("spring.datasource.order1-slave")
    public DataSource dataSourceOrderSlave1(){
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "sqlSessionFactoryOrder")
    @Primary
    public SqlSessionFactory sqlSessionFactoryOrder() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(getDataSource());
        factory.setVfs(SpringBootVFS.class);
        factory.setMapperLocations(resolveMapperLocations());
        return factory.getObject();
    }

    @Bean(name = "sqlSessionTemplateOrder")
    @Primary
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
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration("buyerId,orderNo", new OrderDbComplexKeysShardingAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration("buyerId,orderNo", new OrderTbComplexKeysShardingAlgorithm()));
        shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfigurations());
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new Properties());
    }


    TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_order", "order_ds_master_slave_${0..1}.t_order_${0..3}");
        return result;
    }


    List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration("order_ds_master_slave_0", "dataSourceOrder0", Arrays.asList("dataSourceOrderSlave0"));
        MasterSlaveRuleConfiguration masterSlaveRuleConfig2 = new MasterSlaveRuleConfiguration("order_ds_master_slave_1", "dataSourceOrder1", Arrays.asList("dataSourceOrderSlave1"));
        return Lists.newArrayList(masterSlaveRuleConfig1, masterSlaveRuleConfig2);
    }

    Map<String, DataSource> createDataSourceMap() {
        final Map<String, DataSource> result = new HashMap<>();
        result.put("dataSourceOrder0", dataSourceOrder0());
        result.put("dataSourceOrderSlave0", dataSourceOrderSlave0());
        result.put("dataSourceOrder1", dataSourceOrder1());
        result.put("dataSourceOrderSlave1", dataSourceOrderSlave1());
        return result;
    }
}
