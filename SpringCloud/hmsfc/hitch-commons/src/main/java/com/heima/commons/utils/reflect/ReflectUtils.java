package com.heima.commons.utils.reflect;


import com.heima.commons.utils.reflect.wrap.WrapObject;
import com.heima.commons.utils.reflect.wrap.WrapObjectFactory;
import com.heima.commons.utils.reflect.wrap.WrapProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReflectUtils {


    private static final List<String> BASIC_TYPES = new ArrayList();

    static {
        BASIC_TYPES.add("java.lang.Integer");
        BASIC_TYPES.add("java.lang.Double");
        BASIC_TYPES.add("java.lang.Long");
        BASIC_TYPES.add("java.lang.Short");
        BASIC_TYPES.add("java.lang.Byte");
        BASIC_TYPES.add("java.lang.Boolean");
        BASIC_TYPES.add("java.lang.Character");
        BASIC_TYPES.add("java.lang.String");
        BASIC_TYPES.add("int");
        BASIC_TYPES.add("double");
        BASIC_TYPES.add("long");
        BASIC_TYPES.add("short");
        BASIC_TYPES.add("boolean");
        BASIC_TYPES.add("char");
        BASIC_TYPES.add("float");
    }


    public static boolean isBasicTypes(Class clazz) {
        if (BASIC_TYPES.contains(clazz.getName())) {
            return true;
        }
        return false;
    }

    public static Object getDefValue(Class clazz, String defValue) {
        if (clazz.isAssignableFrom(String.class)) {
            return defValue;
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            return Boolean.parseBoolean(defValue);
        } else if (clazz.isAssignableFrom(Byte.class)) {
            return Byte.parseByte(defValue);
        } else if (clazz.isAssignableFrom(Short.class)) {
            return Short.parseShort(defValue);
        } else if (clazz.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(defValue);
        } else if (clazz.isAssignableFrom(Double.class)) {
            return Double.parseDouble(defValue);
        } else if (clazz.isAssignableFrom(Long.class)) {
            return Long.parseLong(defValue);
        } else if (clazz.isAssignableFrom(Long.class)) {
            return Long.parseLong(defValue);
        }
        return null;
    }


    public static Class classForName(String className) {
        Class clazz = null;
        if (StringUtils.isEmpty(className)) {
            return null;
        }
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {

        }
        return clazz;
    }


    public static List<WrapProperty> getWrapPropertyList(Class clazz) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        if (beanInfo == null) {
            return null;
        }
        PropertyDescriptor[] propertyDescriptorArray = beanInfo.getPropertyDescriptors();
        List<WrapProperty> list = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptorArray) {
            list.add(new WrapProperty(propertyDescriptor));
        }
        return list;
    }


    public static boolean isEquals(Object v1, Object v2) {
        if (v1 == null && v2 == null) {
            return true;
        }
        if (v1 == null && v2 != null) {
            return false;
        }
        if (v1 != null && v2 == null) {
            return false;
        }
        if (v1.equals(v2)) {
            return true;
        }
        return false;
    }


    public static <T> T newInstance(Class clazz) {
        Object value = null;
        try {
            value = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (T) value;
    }

    /**
     * 获取字段的所有Field
     *
     * @param clazz
     * @return
     */
    public static List<Field> getFieldList(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null && !clazz.getName().toLowerCase().equals("java.lang.object")) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

    public static Annotation[] getAnnotations(Field field) {
        if (null != field) {
            return field.getAnnotations();
        }
        return null;
    }

    public static void copyProperties(Object source, Object target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        WrapObject sourceWrap = WrapObjectFactory.getWarpObject(source);
        WrapObject targetWrap = WrapObjectFactory.getWarpObject(target);
        Collection<WrapProperty> properties = sourceWrap.getPropertyList();
        for (WrapProperty property : properties) {
            String propertyName = property.getPropName();
            WrapProperty targetProperty = targetWrap.getProperty(propertyName);
            if (null == targetProperty) {
                continue;
            }
            //字段类型一致
            if (property.getDataType().equals(targetProperty.getDataType())) {
                targetWrap.setValue(propertyName, sourceWrap.getValue(propertyName));
            } else {
                //字段类型不一致处理
            
            }
        }
    }


}
