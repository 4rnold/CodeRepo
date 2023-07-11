package mayfly.sys.biz.common.error;

import mayfly.core.model.result.CodeMessage;

/**
 * sys模块错误码
 *
 * @date 2022-02-12 3:11 PM
 */
public enum SysError implements CodeMessage {

    /**
     * 系统模块断言失败错误码
     */
    ASSERT_ERROR("SYS001", "系统服务断言失败");

    private final String code;

    private final String message;

    SysError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
