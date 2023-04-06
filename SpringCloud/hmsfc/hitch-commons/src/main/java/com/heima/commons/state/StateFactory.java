package com.heima.commons.state;

import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class StateFactory {

    private static final Map<String, State> stateMap = new HashMap();

    static {
        register(AccountState.values());
    }

    public static void register(State[] states) {
        for (State state : states) {
            register(state);
        }
    }

    /**
     * 注册状态
     *
     * @param state
     */
    public static void register(State state) {
        if (stateMap.containsKey(state.getKey())) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_DUPLICATION);
        }
        stateMap.put(state.getKey(), state);
    }

    /**
     * 获取状态对象
     *
     * @param key
     * @return
     */
    public static State getState(String key) {
        return stateMap.get(key);
    }


}
