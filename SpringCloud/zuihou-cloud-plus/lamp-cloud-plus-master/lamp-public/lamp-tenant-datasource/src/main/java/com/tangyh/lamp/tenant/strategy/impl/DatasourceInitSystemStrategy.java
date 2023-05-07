package com.tangyh.lamp.tenant.strategy.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.google.common.collect.Sets;
import com.tangyh.basic.database.mybatis.conditions.Wraps;
import com.tangyh.basic.utils.CollHelper;
import com.tangyh.lamp.common.constant.BizConstant;
import com.tangyh.lamp.tenant.dao.InitDatabaseMapper;
import com.tangyh.lamp.tenant.dto.TenantConnectDTO;
import com.tangyh.lamp.tenant.entity.DatasourceConfig;
import com.tangyh.lamp.tenant.entity.TenantDatasourceConfig;
import com.tangyh.lamp.tenant.enumeration.TenantConnectTypeEnum;
import com.tangyh.lamp.tenant.enumeration.TenantStatusEnum;
import com.tangyh.lamp.tenant.service.DataSourceService;
import com.tangyh.lamp.tenant.service.DatasourceConfigService;
import com.tangyh.lamp.tenant.service.InitDsService;
import com.tangyh.lamp.tenant.service.TenantDatasourceConfigService;
import com.tangyh.lamp.tenant.strategy.InitSystemStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化系统
 * <p>
 * 初始化规则：
 * lamp-authority-server/src/main/resources/sql 路径存放8个sql文件 (每个库对应一个文件)
 * lamp_base.sql            # 基础库：权限、消息，短信，邮件，文件等
 * data_lamp_base.sql       # 基础库数据： 如初始用户，初始角色，初始菜单
 *
 * @author zuihou
 * @date 2019/10/25
 */
@Service("DATASOURCE")
@Slf4j
@RequiredArgsConstructor
public class DatasourceInitSystemStrategy implements InitSystemStrategy {

    private final DatasourceConfigService datasourceConfigService;
    private final TenantDatasourceConfigService tenantDatasourceConfigService;
    private final InitDatabaseMapper initDbMapper;
    private final DataSourceService dataSourceService;
    private final InitDsService initDsService;
    @Value("${spring.application.name:lamp-authority-server}")
    private String applicationName;

    /**
     * 启动项目时，调用初始化数据源
     *
     * @return
     */
    @DS("master")
    public boolean initDataSource() {
        // LOCAL 类型的数据源初始化
        List<String> list = initDbMapper.selectTenantCodeList(TenantStatusEnum.NORMAL.name(), TenantConnectTypeEnum.LOCAL.name());
        list.forEach((tenant) -> {
            dataSourceService.addLocalDynamicRoutingDataSource(tenant);
        });

        // REMOTE 类型的数据源初始化
        List<DatasourceConfig> dcList = datasourceConfigService.listByApplication(applicationName, TenantStatusEnum.NORMAL.name(), TenantConnectTypeEnum.REMOTE.name());
        dcList.forEach(dc -> {
            // 权限服务
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            BeanUtils.copyProperties(dc, dataSourceProperty);
            dataSourceService.addDynamicRoutingDataSource(dataSourceProperty);
        });
        log.debug("初始化租户数据源成功");
        return true;
    }

    /**
     * 求优化！
     *
     * @param tenantConnect 链接信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initConnect(TenantConnectDTO tenantConnect) {
        Map<String, DatasourceConfig> typeMap = new HashMap<>(8);
        if (TenantConnectTypeEnum.REMOTE.eq(tenantConnect.getConnectType())) {

            Long authorityDatasource = tenantConnect.getAuthorityDatasource();
            Long fileDatasource = tenantConnect.getFileDatasource();
            fileDatasource = fileDatasource == null ? authorityDatasource : fileDatasource;
            Long msgsDatasource = tenantConnect.getMsgDatasource();
            msgsDatasource = msgsDatasource == null ? authorityDatasource : msgsDatasource;
            Long oauthDatasource = tenantConnect.getOauthDatasource();
            oauthDatasource = oauthDatasource == null ? authorityDatasource : oauthDatasource;
            Long gateDatasource = tenantConnect.getGateDatasource();
            gateDatasource = gateDatasource == null ? authorityDatasource : gateDatasource;
            List<DatasourceConfig> dcList = datasourceConfigService.listByIds(Sets.newHashSet(authorityDatasource, fileDatasource, msgsDatasource, oauthDatasource, gateDatasource));
            dcList.forEach(item -> {
                item.setType(tenantConnect.getConnectType());
                item.setPoolName(tenantConnect.getTenant());
            });
            Map<Long, DatasourceConfig> dcMap = CollHelper.uniqueIndex(dcList, DatasourceConfig::getId, (data) -> data);

            DatasourceConfig authorityDc = dcMap.get(authorityDatasource);
            typeMap.put(BizConstant.AUTHORITY, authorityDc);
            typeMap.put(BizConstant.FILE, dcMap.getOrDefault(fileDatasource, authorityDc));
            typeMap.put(BizConstant.MSG, dcMap.getOrDefault(msgsDatasource, authorityDc));
            typeMap.put(BizConstant.OAUTH, dcMap.getOrDefault(oauthDatasource, authorityDc));
            typeMap.put(BizConstant.GATE, dcMap.getOrDefault(gateDatasource, authorityDc));

            tenantDatasourceConfigService.remove(Wraps.<TenantDatasourceConfig>lbQ().eq(TenantDatasourceConfig::getTenantId, tenantConnect.getId()));

            List<TenantDatasourceConfig> list = new ArrayList<>();
            list.add(TenantDatasourceConfig.builder().application(BizConstant.AUTHORITY).tenantId(tenantConnect.getId()).datasourceConfigId(authorityDatasource).build());
            list.add(TenantDatasourceConfig.builder().application(BizConstant.FILE).tenantId(tenantConnect.getId()).datasourceConfigId(fileDatasource).build());
            list.add(TenantDatasourceConfig.builder().application(BizConstant.MSG).tenantId(tenantConnect.getId()).datasourceConfigId(msgsDatasource).build());
            list.add(TenantDatasourceConfig.builder().application(BizConstant.OAUTH).tenantId(tenantConnect.getId()).datasourceConfigId(oauthDatasource).build());
            list.add(TenantDatasourceConfig.builder().application(BizConstant.GATE).tenantId(tenantConnect.getId()).datasourceConfigId(gateDatasource).build());
            tenantDatasourceConfigService.saveBatch(list);
        } else {
            String tenant = tenantConnect.getTenant();
            DatasourceConfig dto = new DatasourceConfig();
            dto.setType(tenantConnect.getConnectType());
            dto.setPoolName(tenant);

            typeMap.put(BizConstant.AUTHORITY, dto);
            typeMap.put(BizConstant.FILE, dto);
            typeMap.put(BizConstant.MSG, dto);
            typeMap.put(BizConstant.OAUTH, dto);
            typeMap.put(BizConstant.GATE, dto);
        }

        // 动态初始化数据源
        return initDsService.initConnect(typeMap);
    }

    @Override
    public boolean reset(String tenant) {

        return true;
    }

    @Override
    public boolean delete(List<Long> ids, List<String> tenantCodeList) {
        if (tenantCodeList.isEmpty()) {
            return true;
        }
        tenantDatasourceConfigService.remove(Wraps.<TenantDatasourceConfig>lbQ().in(TenantDatasourceConfig::getTenantId, ids));

        tenantCodeList.forEach(initDsService::removeDataSource);
        return true;
    }
}
