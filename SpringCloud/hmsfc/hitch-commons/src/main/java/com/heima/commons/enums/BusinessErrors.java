package com.heima.commons.enums;

/**
 * 业务错误码
 */
public enum BusinessErrors {
    /**
     * 数据重复异常
     */
    DATA_DUPLICATION(-1001, "数据重复"),
    DATA_NOT_EXIST(-1002, "数据不存在"),
    DATA_STATUS_ERROR(-1003, "数据状态错误"),
    PARAM_CANNOT_EMPTY(-2001, "参数不能为空"),
    AUTHENTICATION_ERROR(-3002, "认证错误"),
    TOKEN_IS_INVALID(-3001, "Token已经失效，重新登录"),
    STOCK_FULL_STARFFED(-5001, "行程已经满员"),
    STOCK_ALREADY_DEPART(-5002, "司机已经发车"),
    PAYMENT_COMMUNICATION_FAILURE(-5004, "行程预支付通讯失败"),
    PAYMENT_PRE_PAY_FAIL(-5005, "行程预支付失败"),
    PAYMENT_PAY_IN_PROGRESSL(-5005, "行程支付进行中"),
    WS_SEND_FAILED(-6001, "websocket发送消息失败");

    //错误码
    private int code;
    //具体错误信息
    private String msg;

    BusinessErrors(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
