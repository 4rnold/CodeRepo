package com.tangyh.lamp.authority.service.common;

import com.tangyh.lamp.authority.entity.common.Parameter;
import com.tangyh.basic.base.service.SuperService;

/**
 * <p>
 * 业务接口
 * 参数配置
 * </p>
 *
 * @author zuihou
 * @date 2020-02-05
 */
public interface ParameterService extends SuperService<Parameter> {
    /**
     * 根据参数键查询参数值
     *
     * @param key    参数键
     * @param defVal 参数值
     * @return 参数值
     */
    String getValue(String key, String defVal);
}
