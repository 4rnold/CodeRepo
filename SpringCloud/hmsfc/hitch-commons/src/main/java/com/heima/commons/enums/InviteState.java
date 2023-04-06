package com.heima.commons.enums;

public enum InviteState {
    // 0 = 未确认， 1 = 已确认 ， 2= 已拒绝
    UNCONFIRMED(0, "未确认"),
    CONFIRMED(1, "已确认"),
    REJECTED(2, "已拒绝"),
    TIMEOUT(3, "已超时");

    InviteState(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static InviteState getState(int code) {
        for (InviteState state : InviteState.values()) {
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