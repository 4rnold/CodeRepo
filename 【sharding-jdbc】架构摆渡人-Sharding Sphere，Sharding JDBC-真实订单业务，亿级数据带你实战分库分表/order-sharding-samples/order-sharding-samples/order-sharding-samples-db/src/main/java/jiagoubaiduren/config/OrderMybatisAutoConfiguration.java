package jiagoubaiduren.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
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
import java.util.Optional;
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

    @Bean(name = "sqlSessionFactoryOrder")
    @Primary
    public SqlSessionFactory sqlSessionFactoryOrder() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSourceOrder());
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
}
