package com.tangyh.lamp.activiti.exception;

import com.tangyh.basic.exception.BizException;
import com.tangyh.basic.exception.code.BaseExceptionCode;

/**
 * 异常抛出
 *
 * @author wz
 * @date 2020-08-21 13:25
 */
public final class MyException {
    private MyException() {
    }

    /**
     * 抛出异常
     *
     * @param ex 异常类型
     */
    public static <T extends BaseExceptionCode> void exception(T ex) {
        throw new BizException(ex.getCode(), ex.getMsg());
    }
}
