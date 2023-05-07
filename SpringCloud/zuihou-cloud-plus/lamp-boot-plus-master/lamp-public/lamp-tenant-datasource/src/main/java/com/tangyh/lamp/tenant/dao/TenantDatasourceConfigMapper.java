package com.tangyh.lamp.tenant.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.tangyh.basic.base.mapper.SuperMapper;
import com.tangyh.lamp.tenant.entity.TenantDatasourceConfig;
import org.springframework.stereotype.Repository;

/**
 * 租户数据源关系 Mapper
 *
 * @author zuihou
 * @date 2020/8/27 下午4:48
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface TenantDatasourceConfigMapper extends SuperMapper<TenantDatasourceConfig> {
}
