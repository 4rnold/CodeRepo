package com.tensquare.user;

import com.tensquare.user.interceptors.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 用户微服务的配置类
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Configuration
public class ApplicationConfigurer extends WebMvcConfigurationSupport{

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 用于注册拦截器的方法
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/*/login");
    }
}
