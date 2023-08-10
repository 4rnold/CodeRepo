package com.arnold;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.HashMap;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put(1, null);
        objectObjectHashMap.put(2, null);
        System.out.println(objectObjectHashMap.keySet().size());
    }
}
