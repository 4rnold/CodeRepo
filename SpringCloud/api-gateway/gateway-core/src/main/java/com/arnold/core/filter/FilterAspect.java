package com.arnold.core.filter;

import lombok.Builder;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FilterAspect {

    String id();

    String name();

    int order() default 0;
}
