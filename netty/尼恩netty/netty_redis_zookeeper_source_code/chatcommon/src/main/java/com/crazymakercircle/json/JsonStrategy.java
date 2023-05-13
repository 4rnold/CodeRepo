package com.crazymakercircle.json;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface JsonStrategy {
    <K, V> Map<K, V> toMap(String json);

    <K, V> Map<K, V> toMap(String json, Type type);

    <T> List<T> toList(String json);

    <T> List<T> toList(String json, Type type);

    String toJson(Object object);

    String toJson(Object object, String dateFormatPattern);

    <T> T fromJson(String json, Class<T> valueType);

    <K, V> Map<K, V> objectToMap(Object fromValue);

    <T> T mapToObject(Map fromMap, Class<T> toValueType);
}