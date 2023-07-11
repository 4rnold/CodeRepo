package mayfly.sys.biz.config;

import mayfly.core.permission.SetLoginAccountInterceptor;
import mayfly.core.web.DefaultGlobalExceptionHandler;
import mayfly.core.web.ResponseAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc配置
 *
 * @author hml
 * @date 2018/6/27 下午3:52
 */
@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 设置登录账号信息拦截器
        registry.addInterceptor(new SetLoginAccountInterceptor()).addPathPatterns("/**");
    }

    /**
     * 全局异常处理器以及统一结果返回
     *
     * @return GlobalExceptionHandler
     */
    @Bean
    public DefaultGlobalExceptionHandler globalExceptionHandler() {
        return new DefaultGlobalExceptionHandler();
    }

    /**
     * 统一包装成功返回结果集
     *
     * @return response advice
     */
    @Bean
    public ResponseAdvice responseAdvice() {
        return new ResponseAdvice();
    }
}
