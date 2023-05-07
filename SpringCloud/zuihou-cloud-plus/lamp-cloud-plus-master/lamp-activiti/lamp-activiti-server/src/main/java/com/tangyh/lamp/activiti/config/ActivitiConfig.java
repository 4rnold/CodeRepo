package com.tangyh.lamp.activiti.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * activiti配置
 *
 * @author wz
 * @date 2020-08-07
 */
@Configuration
public class ActivitiConfig {
    @Bean
    public ProcessEngineConfigurationConfigurer getProcessEngineConfigurationConfigurerImpl() {
        return new ProcessEngineConfigurationConfigurerImpl();
    }


    public static class ProcessEngineConfigurationConfigurerImpl implements ProcessEngineConfigurationConfigurer {
        @Override
        public void configure(SpringProcessEngineConfiguration configuration) {
            configuration.setActivityFontName("宋体");
            configuration.setAnnotationFontName("宋体");
            configuration.setLabelFontName("宋体");
        }
    }
}

