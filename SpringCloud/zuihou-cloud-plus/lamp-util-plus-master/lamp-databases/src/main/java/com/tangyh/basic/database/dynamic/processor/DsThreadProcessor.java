package com.tangyh.basic.database.dynamic.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.tangyh.basic.context.ContextUtil;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 从Thread变量中获取参数
 *
 * @author zuihou
 * @date 2020年03月15日11:12:54
 */
public class DsThreadProcessor extends DsProcessor {

    /**
     * header prefix
     */
    private static final String HEADER_PREFIX = "#thread";

    @Override
    public boolean matches(String key) {
        return key.startsWith(HEADER_PREFIX);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        return ContextUtil.get(key.substring(HEADER_PREFIX.length() + 1));
    }
}
