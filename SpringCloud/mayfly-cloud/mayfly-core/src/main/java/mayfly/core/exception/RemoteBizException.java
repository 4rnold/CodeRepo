package mayfly.core.exception;

import mayfly.core.model.result.CodeMessage;

import java.io.Serial;

/**
 * 远程调用业务异常
 *
 * @author meilin.huang
 * @date 2022-03-19 20:20
 */
public class RemoteBizException extends BaseException {

    @Serial
    private static final long serialVersionUID = -789021883759549647L;

    /**
     * @param errorMsg 错误消息
     */
    public RemoteBizException(CodeMessage errorMsg, Object... params) {
        super(errorMsg, params);
    }

    /**
     * @param errCode 错误code
     * @param msg     错误消息
     */
    public RemoteBizException(String errCode, String msg) {
        super(errCode, msg);
    }
}
