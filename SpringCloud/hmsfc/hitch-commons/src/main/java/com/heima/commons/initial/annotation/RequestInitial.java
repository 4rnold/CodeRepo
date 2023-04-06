package com.heima.commons.initial.annotation;

import com.heima.commons.groups.Group;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface RequestInitial {
    Class<?>[] groups() default Group.All.class;
}
