package com.tangyh.lamp.tenant.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tangyh.basic.base.service.SuperServiceImpl;
import com.tangyh.lamp.tenant.dao.TenantDatasourceConfigMapper;
import com.tangyh.lamp.tenant.entity.TenantDatasourceConfig;
import com.tangyh.lamp.tenant.service.TenantDatasourceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 租户数据源关系
 *
 * @author zuihou
 * @date 2020/8/27 下午4:51
 */
@Slf4j
@Service
@DS("master")
public class TenantDatasourceConfigServiceImpl extends SuperServiceImpl<TenantDatasourceConfigMapper, TenantDatasourceConfig> implements TenantDatasourceConfigService {
}
