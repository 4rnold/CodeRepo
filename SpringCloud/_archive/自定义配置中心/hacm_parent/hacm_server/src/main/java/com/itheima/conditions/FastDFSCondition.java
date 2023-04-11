package com.itheima.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * FastDFS容灾策略加载的条件
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class FastDFSCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //1.获取当前系统环境对象
        Environment environment = context.getEnvironment();
        //2.根据key获取value
        String value = environment.getProperty("service.disaster.tolerance");
        //3.判断value是否为FastDFS
        if("FastDFS".equalsIgnoreCase(value)){
            return true;
        }
        return false;
    }
}
