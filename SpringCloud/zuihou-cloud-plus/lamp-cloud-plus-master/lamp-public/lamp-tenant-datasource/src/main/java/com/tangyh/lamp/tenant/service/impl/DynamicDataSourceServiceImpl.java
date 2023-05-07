package com.tangyh.lamp.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.exception.ErrorCreateDataSourceException;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.tangyh.basic.database.properties.DatabaseProperties;
import com.tangyh.basic.exception.BizException;
import com.tangyh.basic.utils.StrPool;
import com.tangyh.lamp.tenant.dao.InitDatabaseMapper;
import com.tangyh.lamp.tenant.dto.DataSourcePropertyDTO;
import com.tangyh.lamp.tenant.enumeration.TenantConnectTypeEnum;
import com.tangyh.lamp.tenant.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;


/**
 * 数据源管理
 * <p>
 * lamp.database.multiTenantType=DATASOURCE 时，该类才会生效
 *
 * @author zuihou
 * @date 2020年03月15日11:35:08
 */
@Service
@ConditionalOnProperty(prefix = DatabaseProperties.PREFIX, name = "multiTenantType", havingValue = "DATASOURCE")
@Slf4j
@RequiredArgsConstructor
public class DynamicDataSourceServiceImpl implements DataSourceService {
    private static final String SQL_RESOURCE_PATH = "sqls/%s.sql";

    @Value("${lamp.mysql.database}")
    private String defaultDatabase;
    @Value("${lamp.mysql.driverClassName}")
    private String driverClassName;
    @Value("${lamp.mysql.username}")
    private String username;
    @Value("${lamp.mysql.password}")
    private String password;
    @Value("${lamp.mysql.url}")
    private String url;

    private final DataSource dataSource;
    private final DataSourceCreator druidDataSourceCreator;
    private final DatabaseProperties databaseProperties;
    private final DynamicDataSourceProperties properties;
    private final InitDatabaseMapper initDbMapper;

    /**
     * 关闭链接
     *
     * @param connection
     */
    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("链接关闭错误：", e);
            }
        }
    }

    @Override
    public Set<String> findAll() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return ds.getCurrentDataSources().keySet();
    }


    @Override
    public Set<String> remove(String name) {
        // 这里每个服务删除的数据库是自己服务配置的
        String database = new StringBuilder().append(databaseProperties.getTenantDatabasePrefix()).append(StrPool.UNDERSCORE).append(name).toString();
        initDbMapper.dropDatabase(database);

        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(name);
        return ds.getCurrentDataSources().keySet();
    }

    @Override
    public boolean addLocalDynamicRoutingDataSource(String tenant) {
        DataSourceProperty dto = new DataSourceProperty();
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setUrl(StrUtil.replace(url, defaultDatabase, databaseProperties.getTenantDatabasePrefix() + StrUtil.UNDERLINE + tenant));
        dto.setDriverClassName(driverClassName);
        dto.setPoolName(tenant);
        addDynamicRoutingDataSource(dto);
        return true;
    }

    @Override
    public Set<String> addDynamicRoutingDataSource(DataSourceProperty dto) {
        DataSource newDataSource = createDataSource(dto);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) this.dataSource;
        ds.addDataSource(dto.getPoolName(), newDataSource);
        return ds.getCurrentDataSources().keySet();
    }

    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        if (dataSourceProperty == null) {
            dataSourceProperty = new DataSourceProperty();
        }
        dataSourceProperty.setSeata(databaseProperties.getIsSeata());
        dataSourceProperty.setDruid(properties.getDruid());
        try {
            return druidDataSourceCreator.createDataSource(dataSourceProperty);
        } catch (ErrorCreateDataSourceException e) {
            log.error("数据源初始化期间出现异常", e);
            throw new BizException("数据源初始化期间出现异常", e);
        }
    }


    private boolean getTestConnection(DataSourceProperty dataSourceProperty) {
        dataSourceProperty.setSeata(false);
        dataSourceProperty.setDruid(BeanUtil.toBean(properties.getDruid(), DruidConfig.class));
        // 配置获取链接等待超时的时间
        dataSourceProperty.getDruid().setMaxWait(3000);
        // 配置初始化大小、最小、最大
        dataSourceProperty.getDruid().setInitialSize(1);
        dataSourceProperty.getDruid().setMinIdle(1);
        dataSourceProperty.getDruid().setMaxActive(1);
        // 链接错误重试次数
        dataSourceProperty.getDruid().setConnectionErrorRetryAttempts(0);
        // 获取失败后中断
        dataSourceProperty.getDruid().setBreakAfterAcquireFailure(true);

        DataSource testDataSource = null;
        Connection connection = null;
        boolean flag;
        try {
            testDataSource = druidDataSourceCreator.createDataSource(dataSourceProperty);
            connection = testDataSource.getConnection();

            int timeOut = 5;
            if (connection == null || connection.isClosed() || !connection.isValid(timeOut)) {
                log.info("链接已关闭或无效，请重试获取链接！");
                connection = testDataSource.getConnection();
            }
            flag = connection != null;
        } catch (ErrorCreateDataSourceException e) {
            log.error("数据源初始化期间出现异常", e);
            throw new BizException("数据源初始化期间出现异常", e);
        } catch (Exception e) {
            log.error("创建测试链接错误 {}", dataSourceProperty.getUrl());
            throw new BizException("创建测试链接错误 " + dataSourceProperty.getUrl(), e);
        } finally {
            if (testDataSource instanceof ItemDataSource) {
                ((ItemDataSource) testDataSource).close();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.warn("关闭测试数据源链接异常", e);
                }
            }
        }
        return flag;
    }

    /**
     * 测试链接
     *
     * @param dataSourceProperty
     * @return
     */
    @Override
    public boolean testConnection(DataSourceProperty dataSourceProperty) {
        return getTestConnection(dataSourceProperty);
    }

    public void initDatabases(String tenant) {
        this.initDbMapper.createDatabase(StrUtil.join(StrUtil.UNDERLINE, databaseProperties.getTenantDatabasePrefix(), tenant));
    }

    @Override
    public boolean initConnect(DataSourcePropertyDTO dto) {
        if (TenantConnectTypeEnum.LOCAL.eq(dto.getType())) {
            // 创建 库
            this.initDatabases(dto.getPoolName());

            dto.setUsername(username);
            dto.setPassword(password);
            dto.setUrl(StrUtil.replace(url, defaultDatabase, databaseProperties.getTenantDatabasePrefix() + StrUtil.UNDERLINE + dto.getPoolName()));
            dto.setDriverClassName(driverClassName);
        }
        // 链接
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        BeanUtils.copyProperties(dto, dataSourceProperty);
        addDynamicRoutingDataSource(dataSourceProperty);

        ScriptRunner runner = this.getScriptRunner(dataSourceProperty.getPoolName());
        //创建表
        this.initTables(runner);

        //初始化数据
        this.initData(runner);
        return true;
    }

    public void initTables(ScriptRunner runner) {
        try {
            Reader resourceAsReader = Resources.getResourceAsReader(String.format(SQL_RESOURCE_PATH, databaseProperties.getTenantDatabasePrefix()));
            runner.runScript(resourceAsReader);
        } catch (Exception e) {
            log.error("初始化表失败", e);
            throw new BizException("初始化表失败", e);
        }
    }

    /**
     * 角色表
     * 菜单表
     * 资源表
     *
     * @param runner
     */
    public void initData(ScriptRunner runner) {
        try {
            String dataScript = databaseProperties.getTenantDatabasePrefix() + "_data";
            runner.runScript(Resources.getResourceAsReader(String.format(SQL_RESOURCE_PATH, dataScript)));
        } catch (Exception e) {
            log.error("初始化数据失败", e);
            throw new BizException("初始化数据失败", e);
        }
    }

    @SuppressWarnings("AlibabaRemoveCommentedCode")
    public ScriptRunner getScriptRunner(String tenant) {
        try {
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) this.dataSource;
            DataSource curDataSource = ds.getDataSource(tenant);
            Connection connection = curDataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(false);
            //遇见错误是否停止
            runner.setStopOnError(true);
            /*
             * 按照那种方式执行 方式一：true则获取整个脚本并执行； 方式二：false则按照自定义的分隔符每行执行；
             */
            runner.setSendFullScript(true);

            Resources.setCharset(Charset.forName("UTF8"));

            runner.setFullLineDelimiter(false);
            return runner;
        } catch (Exception ex) {
            throw new BizException("获取链接失败", ex);
        }
    }

}
