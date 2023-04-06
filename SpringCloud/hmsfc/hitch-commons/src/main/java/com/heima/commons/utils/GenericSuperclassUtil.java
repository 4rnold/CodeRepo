package com.heima.commons.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericSuperclassUtil {

    /*
     * 获取泛型类Class对象，不是泛型类则返回null
     */
    public static <T> Class<T> getActualTypeArgument(Class clazz) {
        Class<T> entitiClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                entitiClass = (Class<T>) actualTypeArguments[0];
            }
        }

        return entitiClass;
    }

}
