package com.tangyh.lamp.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.tangyh.basic.base.service.SuperServiceImpl;
import com.tangyh.lamp.tenant.dao.DatasourceConfigMapper;
import com.tangyh.lamp.tenant.entity.DatasourceConfig;
import com.tangyh.lamp.tenant.service.DataSourceService;
import com.tangyh.lamp.tenant.service.DatasourceConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务实现类
 * 数据源
 * </p>
 *
 * @author zuihou
 * @date 2020-08-21
 */
@Slf4j
@Service
@DS("master")
@RequiredArgsConstructor
public class DatasourceConfigServiceImpl extends SuperServiceImpl<DatasourceConfigMapper, DatasourceConfig> implements DatasourceConfigService {

    private final DataSourceService dataSourceService;

    @Override
    public Boolean testConnection(DataSourceProperty dataSourceProperty) {
        return dataSourceService.testConnection(dataSourceProperty);
    }

    @Override
    public List<DatasourceConfig> listByApplication(String applicationName, String status, String connectType) {
        return baseMapper.listByApplication(applicationName, status, connectType);
    }
}
