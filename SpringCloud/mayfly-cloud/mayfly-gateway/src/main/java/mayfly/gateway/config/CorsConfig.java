package mayfly.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author meilin.huang
 * @date 2022-03-21 17:26
 */
@Configuration(proxyBeanMethods = false)
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许哪些头进行跨域
        corsConfiguration.addAllowedHeader("*");
        //允许哪些请求方式进行跨域
        corsConfiguration.addAllowedMethod("*");
        //允许哪些请求来源进行跨域
        corsConfiguration.addAllowedOriginPattern("*");
        //是否允许携带cookie进行跨域，否则跨域请求会丢失cookie信息
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }
}
