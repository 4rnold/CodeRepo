package com.heima.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocalCollectionUtils {
    /**
     * 数组转List
     *
     * @param arrays
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(T... arrays) {
        List<T> tList = emptyList();
        for (T t : arrays) {
            tList.add(t);
        }
        return tList;
    }

    public static <T> T getOne(List<T> tList) {
        if (null != tList && !tList.isEmpty()) {
            return tList.stream().findFirst().get();
        }
        return null;
    }

    /**
     * 创建一个新的List
     *
     * @param <T>
     * @return
     */
    public static <T> List<T> emptyList() {
        return new ArrayList<T>();
    }

    public static String toString(List<String> stringList) {
        if (null == stringList) {
            return null;
        }
        if (stringList.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String str : stringList) {
            sb.append(str).append(",");
        }
        return StringUtils.removeEnd(sb.toString(), ",");
    }
}
