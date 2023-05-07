package com.tangyh.lamp.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tangyh.basic.mq.properties.MqProperties;
import com.tangyh.lamp.common.constant.BizConstant;
import com.tangyh.lamp.tenant.dto.DataSourcePropertyDTO;
import com.tangyh.lamp.tenant.entity.DatasourceConfig;
import com.tangyh.lamp.tenant.service.DataSourceService;
import com.tangyh.lamp.tenant.service.InitDsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 没有开启消息队列就只能轮训了
 *
 * @author zuihou
 * @date 2020年04月05日16:27:03
 */
@Service
@Slf4j
@ConditionalOnProperty(prefix = MqProperties.PREFIX, name = "enabled", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class DefaultInitDsServiceImpl implements InitDsService {

    private final DataSourceService dataSourceService;

    @Override
    public boolean removeDataSource(String tenant) {
        // 权限服务
        dataSourceService.remove(tenant);
        return true;
    }

    @Override
    public boolean initConnect(Map<String, DatasourceConfig> typeMap) {
        // 权限服务
        DataSourcePropertyDTO authorityDsp = BeanUtil.toBean(typeMap.get(BizConstant.AUTHORITY), DataSourcePropertyDTO.class);
        return dataSourceService.initConnect(authorityDsp);
    }
}
