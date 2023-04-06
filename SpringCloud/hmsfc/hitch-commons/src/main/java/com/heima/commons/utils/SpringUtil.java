package com.heima.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 根据类型获取bean (不需要注入，直接调静态方法）
     *
     * @param clazz springBean类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return (T) context.getBean(clazz);
    }

    /**
     * 根据bean名获取bean
     *
     * @param beanName bean名
     * @return
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    /**
     * 根据bean 类型和名字确定唯一bean
     *
     * @param beanName bean 名
     * @param clazz    类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        T bean = context.getBean(beanName, clazz);
        return bean;
    }
}