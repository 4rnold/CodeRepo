package com.heima.commons.utils.reflect.wrap;

import com.heima.commons.utils.reflect.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WrapClass {
    private Class classType;
    private Map<String, WrapProperty> propertyMap = new ConcurrentHashMap<>();
    private Map<String, Field> fieldMap = new ConcurrentHashMap<>();

    public WrapClass(Class clazz) {
        this.classType = clazz;
        List<Field> fieldList = ReflectUtils.getFieldList(clazz);
        fieldList.forEach(field -> addField(field));
        List<WrapProperty> propertyList = ReflectUtils.getWrapPropertyList(clazz);
        propertyList.forEach(prop -> addProperty(prop));

    }

    private void addProperty(WrapProperty prop) {
        if (!propertyMap.containsKey(prop.getPropName())) {
            Annotation[] annotations = ReflectUtils.getAnnotations(fieldMap.get(prop.getPropName()));
            prop.setAnnotations(annotations);
            propertyMap.put(prop.getPropName(), prop);
        }
    }

    private void addField(Field field) {
        if (!fieldMap.containsKey(field.getName())) {
            fieldMap.put(field.getName(), field);
        }
    }

    public WrapProperty getProperty(String propName) {
        return propertyMap.get(propName);
    }


    public Class getClassType() {
        return classType;
    }


    public Map<String, WrapProperty> getPropertyMap() {
        return propertyMap;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }
}
