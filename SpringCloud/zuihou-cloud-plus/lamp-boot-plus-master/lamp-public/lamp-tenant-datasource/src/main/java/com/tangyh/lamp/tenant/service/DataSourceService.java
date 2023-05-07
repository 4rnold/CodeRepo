package com.tangyh.lamp.tenant.service;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.tangyh.lamp.tenant.dto.DataSourcePropertyDTO;

import java.util.Set;

/**
 * 数据源管理
 *
 * @author zuihou
 * @date 2020年03月15日11:31:57
 */
public interface DataSourceService {
    /**
     * 查询所有的数据源
     *
     * @return
     */
    Set<String> findAll();

    /**
     * 新增Druid数据源
     *
     * @param dto
     * @return
     */
    Set<String> addDynamicRoutingDataSource(DataSourceProperty dto);

    /**
     * 新增Druid数据源
     *
     * @param tenant
     * @return
     */
    boolean addLocalDynamicRoutingDataSource(String tenant);

    /**
     * 删除指定的数据源
     *
     * @param name
     * @return
     */
    Set<String> remove(String name);

    /**
     * 测试链接
     *
     * @param dataSourceProperty
     * @return
     */
    boolean testConnection(DataSourceProperty dataSourceProperty);

    /**
     * 初始化 数据源链接
     *
     * @param dataSourceProperty
     * @return
     */
    boolean initConnect(DataSourcePropertyDTO dataSourceProperty);
}
