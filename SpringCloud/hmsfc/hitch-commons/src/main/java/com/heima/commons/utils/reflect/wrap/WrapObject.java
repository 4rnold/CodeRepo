package com.heima.commons.utils.reflect.wrap;

import java.util.Collection;

public class WrapObject {
    private WrapClass wrapClass;
    private Object originalData;


    public WrapObject(Object originalData, WrapClass wrapClass) {
        if (originalData == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.originalData = originalData;
        this.wrapClass = wrapClass;

    }


    public Object getValue(String propName) {
        WrapProperty meateProp = wrapClass.getProperty(propName);
        if (null == meateProp) {
            return null;
        }
        return meateProp.getValue(originalData);
    }

    public Object setValue(String propName, Object... args) {
        WrapProperty meateProp = wrapClass.getProperty(propName);
        if (null == meateProp) {
            return null;
        }
        return meateProp.setValue(originalData, args);
    }

    public WrapProperty getProperty(String key) {
        return wrapClass.getPropertyMap().get(key);
    }

    public Collection<WrapProperty> getPropertyList() {
        return wrapClass.getPropertyMap().values();
    }

}
