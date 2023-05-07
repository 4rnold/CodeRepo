package com.tangyh.lamp.gateway.filter.zuul;

import com.tangyh.basic.jwt.TokenUtil;
import com.tangyh.lamp.common.properties.IgnoreProperties;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 公共配置类, 一些公共工具配置
 *
 * @author zuihou
 * @date 2018/8/25
 */
@AllArgsConstructor
public class ZuulMvcConfigurer implements WebMvcConfigurer {

    private final TokenUtil tokenUtil;
    private final String multiTenantType;
    private final IgnoreProperties ignoreTokenProperties;
    /**
     * 注册 拦截器
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ZuulTokenContextInterceptor(tokenUtil, multiTenantType, ignoreTokenProperties))
                .addPathPatterns("/**")
                .order(1);
    }
}
