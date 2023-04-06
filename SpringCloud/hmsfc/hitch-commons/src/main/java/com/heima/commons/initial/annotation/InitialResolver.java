package com.heima.commons.initial.annotation;

import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Documented
public @interface InitialResolver {
    InitialResolverType resolver();

    Class<?>[] groups() default Group.All.class;

    //默认值
    String def() default "";
}
