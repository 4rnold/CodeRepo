package com.heima.commons.state;

public enum AccountState implements State {
    NORMAL("NORMAL", "正常"),
    DISABLED("DISABLED", "禁用");

    AccountState(String key, String describe) {
        this.key = key;
        this.describe = describe;
    }


    private String key;
    private String describe;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public State[] getValues() {
        return values();
    }

    @Override
    public boolean is(State state) {
        if (this.key.equals(state.getKey())) {
            return true;
        }
        return false;
    }

    public String getDescribe() {
        return describe;
    }
}
