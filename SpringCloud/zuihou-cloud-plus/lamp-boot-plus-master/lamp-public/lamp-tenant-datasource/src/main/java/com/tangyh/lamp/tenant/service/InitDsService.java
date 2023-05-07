package com.tangyh.lamp.tenant.service;


import com.tangyh.lamp.tenant.entity.DatasourceConfig;

import java.util.Map;

/**
 * 广播初始化数据源
 *
 * @author zuihou
 * @date 2020年04月05日16:27:03
 */
public interface InitDsService {
    /**
     * 删除数据源
     *
     * @param tenant
     * @return
     */
    boolean removeDataSource(String tenant);

    /**
     * 初始化 数据源
     *
     * @param typeMap 租户
     * @return 是否成功
     */
    boolean initConnect(Map<String, DatasourceConfig> typeMap);
}
