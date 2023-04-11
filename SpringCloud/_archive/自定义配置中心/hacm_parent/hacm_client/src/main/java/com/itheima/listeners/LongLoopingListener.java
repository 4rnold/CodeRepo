package com.itheima.listeners;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.itheima.context.pull.PullConfigUtil;
import com.itheima.context.refresh.ContextRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.Properties;

/**
 * 通过定时向服务端发送请求，来获取最新配置
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Component
@EnableScheduling
public class LongLoopingListener {

    @Autowired
    private ContextRefresher contextRefresher;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    /**
     * 定期执行
     */
    @Scheduled(fixedRate = 50l)
    public void synchronizedServiceConfig(){
        //1.获取服务端的配置
        Properties properties = PullConfigUtil.findConfig();
        //2.比较
        boolean isRefresh = this.checkConfigInfo(properties);
        //3.判断
        if(isRefresh){
            contextRefresher.refresh(properties);
        }
    }

    /**
     * 检查是否需要更新的方法
     * @param properties
     * @return
     */
    private boolean checkConfigInfo(Properties properties){
        //1.取出本地配置信息
        MutablePropertySources mutablePropertySources = configurableEnvironment.getPropertySources();
        //2.取出application.yml的配置内容
        PropertySource propertySource = mutablePropertySources.get("applicationConfig: [classpath:/application.yml]");
        //3.遍历服务器端的配置
        Enumeration keys = properties.keys();
        while (keys.hasMoreElements()){
            //取出key和value
            String key = (String)keys.nextElement();
            String value = (String)properties.get(key);
            //判断本地的配置中是否有此key
            if(propertySource.containsProperty(key)){
                //有key,取出本地的value
                String localValue = (String)propertySource.getProperty(key);
                //比较
                if(!value.equals(localValue)){
                    return true;
                }
            }else {
                //没有key
                return true;
            }
        }
        return false;
    }
}
