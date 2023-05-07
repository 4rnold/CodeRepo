package com.tangyh.lamp.gateway.config;

import com.tangyh.basic.boot.config.BaseConfig;
import com.tangyh.basic.jwt.TokenUtil;
import com.tangyh.lamp.common.properties.IgnoreProperties;
import com.tangyh.lamp.gateway.filter.gateway.PreCheckFilter;
import com.tangyh.lamp.gateway.filter.zuul.ZuulMvcConfigurer;
import com.tangyh.lamp.gateway.filter.zuul.ZuulPreCheckFilter;
import com.tangyh.lamp.gateway.service.BlockListService;
import com.tangyh.lamp.gateway.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zuihou
 * @date 2017-12-15 14:42
 */
@Configuration
public class GatewayWebConfiguration extends BaseConfig {

    /**
     * lamp-zuul-server 服务在配置文件中配置 lamp.webmvc.enabled=true ，会加载下面2个Bean
     */
    @ConditionalOnProperty(prefix = "lamp.webmvc", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class WebmvcConfig {
        /**
         * zuul服务 限流 + 阻止访问 功能的过滤器
         *
         * @param blockListService   阻止列表Service （spring自动注入）
         * @param rateLimiterService 限流Service （spring自动注入）
         */
        @Bean
        public ZuulPreCheckFilter getZuulPreCheckFilter(BlockListService blockListService, RateLimiterService rateLimiterService) {
            return new ZuulPreCheckFilter(blockListService, rateLimiterService);
        }

        @Bean
        public ZuulMvcConfigurer getZuulMvcConfigurer(TokenUtil tokenUtil, @Value("${lamp.database.multiTenantType:SCHEMA}") String multiTenantType,
                                                      IgnoreProperties ignoreTokenProperties) {
            return new ZuulMvcConfigurer(tokenUtil, multiTenantType, ignoreTokenProperties);
        }

    }

    /**
     * lamp-gateway-server 服务在配置文件中配置 lamp.webmvc.enabled=false ，会加载下面1个Bean
     */
    @ConditionalOnProperty(prefix = "lamp.webmvc", name = "enabled", havingValue = "false", matchIfMissing = true)
    public static class WebfluxConfig {
        /**
         * gateway服务 限流 + 阻止访问 功能的过滤器
         *
         * @param blockListService   阻止列表Service （spring自动注入）
         * @param rateLimiterService 限流Service （spring自动注入）
         */
        @Bean
        public PreCheckFilter getPreCheckFilter(BlockListService blockListService, RateLimiterService rateLimiterService) {
            return new PreCheckFilter(blockListService, rateLimiterService);
        }
    }
}
