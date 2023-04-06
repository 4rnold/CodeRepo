package com.heima.commons.enums;

import com.heima.commons.initial.realize.CurrentDateInitialParser;
import com.heima.commons.initial.realize.CurrentUserInitialParser;
import com.heima.commons.initial.realize.DefaultValueInitialParser;
import com.heima.commons.initial.realize.SnowflakeIDInitialParser;

public enum InitialResolverType {
    CURRENT_DATE("CURRENT_DATE", CurrentDateInitialParser.class),
    CURRENTA_ACCOUNT("CURRENTA_ACCOUNT", CurrentUserInitialParser.class),
    GEN_SNOWFLAKE_ID("GEN_SNOWFLAKE_ID", SnowflakeIDInitialParser.class),
    DEF_VALUE("DEF_VALUE", DefaultValueInitialParser.class);

    InitialResolverType(String resolverName, Class resolverClass) {
        this.resolverName = resolverName;
        this.resolverClass = resolverClass;
    }


    //错误码
    private String resolverName;
    //具体错误信息
    private Class resolverClass;

    public String getResolverName() {
        return resolverName;
    }

    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
    }

    public Class getResolverClass() {
        return resolverClass;
    }

    public void setResolverClass(Class resolverClass) {
        this.resolverClass = resolverClass;
    }
}
