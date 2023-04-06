package com.heima.commons.utils.reflect.wrap;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 元数据属性
 */
public class WrapProperty {


    private String propName;
    private Class dataType;
    private Method readMethod;
    private Method writeMethod;
    private Annotation[] annotations;


    public WrapProperty(PropertyDescriptor propertyDescriptor) {
        this.propName = propertyDescriptor.getName();
        this.dataType = propertyDescriptor.getPropertyType();
        this.readMethod = propertyDescriptor.getReadMethod();
        this.writeMethod = propertyDescriptor.getWriteMethod();
    }

    public Object getValue(Object originalData) {
        if (null == readMethod) {
            return null;
        }
        try {
            return readMethod.invoke(originalData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setValue(Object originalData, Object... args) {

        boolean isSuccess = false;
        if (null == writeMethod) {
            return isSuccess;
        }
        try {
            writeMethod.invoke(originalData, args);
            isSuccess = true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public Class getDataType() {
        return dataType;
    }

    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }
}
