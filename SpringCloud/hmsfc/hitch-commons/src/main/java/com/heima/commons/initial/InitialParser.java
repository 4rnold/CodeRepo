package com.heima.commons.initial;

import com.heima.commons.initial.annotation.InitialResolver;

public interface InitialParser {

    boolean isMatch(Class clazz);


    Object getDefaultValue(Class clazz, InitialResolver initialResolver);
}
