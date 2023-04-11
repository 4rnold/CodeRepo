package com.itheima.annotations;

import com.itheima.context.postprocessor.EnvironmentConfigPostProcessor;
import com.itheima.context.refresh.ContextRefresher;
import com.itheima.controller.ContextRefreshController;
import com.itheima.listeners.ContextRefreshListener;
import com.itheima.listeners.LongLoopingListener;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 定义开启配置中心客户端支持的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ContextRefreshController.class, ContextRefresher.class, ContextRefreshListener.class, LongLoopingListener.class})
public @interface EnableConfigClient {

}
