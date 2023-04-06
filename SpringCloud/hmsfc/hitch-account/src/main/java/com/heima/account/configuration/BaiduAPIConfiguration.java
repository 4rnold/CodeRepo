package com.heima.account.configuration;

import com.heima.commons.ai.BaiduAIHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class BaiduAPIConfiguration {


    @Value("${baidu.apikey}")
    private String apiKey;
    @Value("${baidu.secretkey}")
    private String secretKey;


    @Bean
    public BaiduAIHelper baiduAIHelper(StringRedisTemplate redisTemplate) {
        return new BaiduAIHelper(apiKey, secretKey, redisTemplate);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
