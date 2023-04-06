package com.heima.commons.utils.reflect.wrap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WrapObjectFactory {
    private static final Map<Class, WrapClass> wrapClassMap = new ConcurrentHashMap<>();

    public static WrapObject getWarpObject(Object orginObject) {
        WrapClass wrapClass = getClass(orginObject.getClass());
        return new WrapObject(orginObject, wrapClass);
    }

    private static WrapClass getClass(Class clazz) {
        return wrapClassMap.computeIfAbsent(clazz, k -> new WrapClass(k));
    }
}
