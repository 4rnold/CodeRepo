package com.heima.commons.enums;

/**
 * 闪电确认状态
 */
public enum QuickConfirmState {
    DISABLED(0, "未开启"),
    ENABLED(1, "已确认");

    QuickConfirmState(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static QuickConfirmState getState(int code) {
        for (QuickConfirmState state : QuickConfirmState.values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        return null;
    }

    private int code;
    private String describe;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}