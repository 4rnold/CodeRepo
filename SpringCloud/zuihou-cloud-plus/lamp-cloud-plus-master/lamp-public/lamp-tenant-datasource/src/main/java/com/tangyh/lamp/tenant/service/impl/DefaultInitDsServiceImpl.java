package com.tangyh.lamp.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tangyh.basic.base.R;
import com.tangyh.basic.exception.BizException;
import com.tangyh.basic.mq.properties.MqProperties;
import com.tangyh.lamp.common.constant.BizConstant;
import com.tangyh.lamp.tenant.api.AuthorityDsApi;
import com.tangyh.lamp.tenant.api.FileDsApi;
import com.tangyh.lamp.tenant.api.GatewayDsApi;
import com.tangyh.lamp.tenant.api.MsgDsApi;
import com.tangyh.lamp.tenant.api.OauthDsApi;
import com.tangyh.lamp.tenant.dto.DataSourcePropertyDTO;
import com.tangyh.lamp.tenant.entity.DatasourceConfig;
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

    private final AuthorityDsApi authorityDsApi;
    private final OauthDsApi oauthDsApi;
    private final FileDsApi fileDsApi;
    private final MsgDsApi msgDsApi;
    private final GatewayDsApi gatewayDsApi;

    @Override
    public boolean removeDataSource(String tenant) {
        // 权限服务
        R<Boolean> authority = authorityDsApi.remove(tenant);
        // SpringBoot项目就不需要调用以下方法了
        // oauth
        R<Boolean> oauth = oauthDsApi.remove(tenant);
        // file
        R<Boolean> file = fileDsApi.remove(tenant);
        // msg
        R<Boolean> msg = msgDsApi.remove(tenant);

        R<Boolean> gateway = gatewayDsApi.remove(tenant);
        log.info("authority={}", authority);
        log.info("gateway={} ", gateway);
        log.info("oauth={}, file={}, msg={}", oauth, file, msg);
        return true;
    }

    @Override
    public boolean initConnect(Map<String, DatasourceConfig> typeMap) {

        // 权限服务
        DataSourcePropertyDTO authorityDsp = BeanUtil.toBean(typeMap.get(BizConstant.AUTHORITY), DataSourcePropertyDTO.class);
        R<Boolean> authority = authorityDsApi.initConnect(authorityDsp);

        // SpringBoot项目就不需要调用以下方法了
        // oauth
        DataSourcePropertyDTO oauthDsp = BeanUtil.toBean(typeMap.get(BizConstant.OAUTH), DataSourcePropertyDTO.class);
        R<Boolean> oauth = oauthDsApi.initConnect(oauthDsp);
        // file
        DataSourcePropertyDTO fileDsp = BeanUtil.toBean(typeMap.get(BizConstant.FILE), DataSourcePropertyDTO.class);
        R<Boolean> file = fileDsApi.initConnect(fileDsp);
        // msg
        DataSourcePropertyDTO msgDsp = BeanUtil.toBean(typeMap.get(BizConstant.MSG), DataSourcePropertyDTO.class);
        R<Boolean> msg = msgDsApi.initConnect(msgDsp);
        // 网关
        DataSourcePropertyDTO gateDsp = BeanUtil.toBean(typeMap.get(BizConstant.GATE), DataSourcePropertyDTO.class);
        R<Boolean> gate = gatewayDsApi.initConnect(gateDsp);
        // 其他业务
        log.info("authority={}", authority);
        log.info("gateway={} ", gate);
        log.info("oauth={}, file={}, msg={}", oauth, file, msg);
        if (!gate.getIsSuccess() || gate.getData() == null || !gate.getData()) {
            throw new BizException("初始化网关服务数据源异常:" + gate.getMsg());
        }
        if (!oauth.getIsSuccess() || oauth.getData() == null || !oauth.getData()) {
            throw new BizException("初始化认证服务数据源异常:" + oauth.getMsg());
        }
        // 需要将全部服务的数据源连接成功，才叫成功，任意一个服务连接失败，都需要重新连接。开发环境，仅仅启动几个服务时，自行将未启动的服务注释掉，保证代码正确执行
        if (!authority.getIsSuccess() || authority.getData() == null || !authority.getData()) {
            throw new BizException("初始化权限服务数据源异常:" + authority.getMsg());
        }
        if (!file.getIsSuccess() || file.getData() == null || !file.getData()) {
            throw new BizException("初始化文件服务数据源异常:" + file.getMsg());
        }
        if (!msg.getIsSuccess() || msg.getData() == null || !msg.getData()) {
            throw new BizException("初始化消息服务数据源异常:" + authority.getMsg());
        }
        return true;
    }
}
