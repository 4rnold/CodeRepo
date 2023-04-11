package com.itheima.context.postprocessor;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.itheima.context.pull.PullConfigUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 用于容器启动时读取服务器端的微服务配置
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class EnvironmentConfigPostProcessor implements EnvironmentPostProcessor {

    /**
     * 读取最新服务器端的配置，并存入Spring的环境对象中
     * @param environment
     * @param application
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("begin...");
        //1.读配置中心服务端的最新配置
        Properties properties = PullConfigUtil.findConfig();
        //2.判断properties是否有值
        if(properties == null){
            throw new ExceptionInInitializerError("初始化系统配置失败！");
        }
        //3.创建Spring的PropertySource
        PropertySource mapPropertySource = new PropertiesPropertySource("applicationConfig: [classpath:/application.yml]",properties);
        //4.从系统环境变量中获取存放PropertySource的对象
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        //5.判断是否已经有了
        PropertySource propertySource = mutablePropertySources.get("applicationConfig: [classpath:/application.yml]");
        if(propertySource == null){
            //没有，添加配置
            environment.getPropertySources().addLast(mapPropertySource);
        }else {
            //有了，替换
            environment.getPropertySources().replace("applicationConfig: [classpath:/application.yml]",mapPropertySource);
        }
        application.setEnvironment(environment);
    }
}
