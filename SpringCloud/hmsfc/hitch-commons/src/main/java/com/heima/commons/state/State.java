package com.heima.commons.state;

public interface State {
    /**
     * 获取Key
     *
     * @return
     */
    String getKey();

    /**
     * 获取
     *
     * @return
     */
    State[] getValues();

    public boolean is(State state);


    /**
     * 获取状态对象
     *
     * @param key
     * @return
     */
    static State getState(String key) {
        return StateFactory.getState(key);
    }


}
