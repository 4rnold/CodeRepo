package com.arnold;

import com.arnold.core.Config;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class TestConcurrentHashmap {

    @Test
    public void test111() {
        ConcurrentHashMap<String, Config> stringConfigConcurrentHashMap = new ConcurrentHashMap<>();
        stringConfigConcurrentHashMap.putIfAbsent("111", new Config());
        stringConfigConcurrentHashMap.putIfAbsent("111", new Config());


    }
}
