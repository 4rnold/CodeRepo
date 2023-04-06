package com.heima.commons.initial.realize;

import com.heima.commons.initial.InitialParser;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.commons.utils.SnowflakeIdWorker;

/**
 * 雪花ID生成器
 */
public class SnowflakeIDInitialParser implements InitialParser {
    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(10, 11);

    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return String.valueOf(snowflakeIdWorker.nextId());
    }
}
