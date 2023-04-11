package com.itheima.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 缓存策略Redis加载的条件
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class RedisCondition implements Condition {

    /**
     * 用于编写条件，当返回值是true的时候，使用@Conditional注解并提供此类字节码的bean会被注入
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //1.获取当前系统环境对象
        Environment environment = context.getEnvironment();
        //2.根据key获取value
        String value = environment.getProperty("service.cache");
        //3.判断value是否为redis
        if("redis".equalsIgnoreCase(value)){
            return true;
        }
        return false;
    }
}
