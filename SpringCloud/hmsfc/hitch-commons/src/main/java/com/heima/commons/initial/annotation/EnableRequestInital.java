package com.heima.commons.initial.annotation;

import com.heima.commons.initial.aspect.RequestInitialAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动参数初始化功能
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(RequestInitialAspect.class)
@Documented
public @interface EnableRequestInital {

}
