/**
 * @projectName JianGateWay
 * @package tech.songjian.gateway.config.center.nacos
 * @className tech.songjian.gateway.config.center.nacos.NacosConfigCenter
 */
package com.arnold.gateway.config.center.nacos;


import cn.hutool.json.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.arnold.common.rule.Rule;
import com.arnold.common.utils.JSONUtil;
import com.arnold.gateway.config.center.ConfigInfo;
import com.arnold.gateway.config.center.api.ConfigCenter;
import com.arnold.gateway.config.center.api.RulesChangeListener;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.concurrent.Executor;

/**
 * NacosConfigCenter
 * @description
 * @author SongJian
 * @date 2023/6/10 14:12
 * @version
 */
@Slf4j
public class NacosConfigCenter implements ConfigCenter {

    /**
     * 定义常量 DATA_ID
     */
    private static final String DATA_ID = "api-gateway";

    /**
     * 服务端地址
     */
    private String serverAddr;

    /**
     * 环境
     */
    private String env;

    /**
     * nacos 提供的便于获取 配置的 api
     */
    private ConfigService configService;

    @Override
    public void init(String serverAddr, String env) {
        this.serverAddr = serverAddr;
        this.env = env;

        try {
            configService  = NacosFactory.createConfigService(serverAddr);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 1. 初始化rule到本地
     * 2. 添加configService的listener，listener收到消息，缓存到本地。
     * @param listener
     */
    @Override
    public void subscribeRulesChange(RulesChangeListener listener) {
        try {
            // 最开始先获取一次配置
            String config = configService.getConfig(DATA_ID, env, 5000);
            log.info("【配置中心】从 Nacos 中获取配置: {}", config);

            // JSON 转化成 JAVA 对象
            ConfigInfo configInfo = JSONUtil.parse(config, ConfigInfo.class);

//            String ruleListJson = JSONUtil.parseOneField(config, "rules");
//            List<Rule> rules = JSONUtil.parseToList(ruleListJson, Rule.class);
//            List<Rule> rules = JSONUtil.parseObject(config).getJSONArray("rules").toJavaList(Rule.class);
            listener.onRulesChange(configInfo.getRules());

            // addListener 添加监听器。监听配置变化
            configService.addListener(DATA_ID, env, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String config) {
                    log.info("【配置中心】从 Nacos 中获取配置: {}", config);
                    ConfigInfo configInfo = JSONUtil.parse(config, ConfigInfo.class);

                    // 缓存到本地
                    listener.onRulesChange(configInfo.getRules());
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}

