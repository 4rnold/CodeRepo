package com.heima.commons.initial.realize;


import com.heima.commons.initial.InitialParser;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.commons.utils.reflect.ReflectUtils;

public class DefaultValueInitialParser implements InitialParser {
    @Override
    public boolean isMatch(Class clazz) {
        return ReflectUtils.isBasicTypes(clazz);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        Object defValue = null;
        try {
            defValue = ReflectUtils.getDefValue(clazz, initialResolver.def());
        } catch (Exception e) {

        }
        return defValue;
    }
}
