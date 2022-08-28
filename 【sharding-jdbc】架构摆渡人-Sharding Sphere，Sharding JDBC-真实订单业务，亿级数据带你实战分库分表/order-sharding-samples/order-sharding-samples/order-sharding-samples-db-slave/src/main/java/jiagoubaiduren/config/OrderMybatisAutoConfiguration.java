package jiagoubaiduren.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
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

    @Bean(name = "dataSourceOrder")
    @Primary
    @ConfigurationProperties("spring.datasource.order")
    public DataSource dataSourceOrder(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceOrderSlave")
    @ConfigurationProperties("spring.datasource.order-slave")
    public DataSource dataSourceOrderSlave(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactoryOrder")
    @Primary
    public SqlSessionFactory sqlSessionFactoryOrder() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(getMasterSlaveDataSource());
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

    private DataSource getMasterSlaveDataSource() throws SQLException {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration(
                "order_ds_master_slave", "dataSourceOrder", Arrays.asList("dataSourceOrderSlave"));
        return MasterSlaveDataSourceFactory.createDataSource(createDataSourceMap(), masterSlaveRuleConfig, new Properties());
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("dataSourceOrder", dataSourceOrder());
        result.put("dataSourceOrderSlave", dataSourceOrderSlave());
        return result;
    }
}
