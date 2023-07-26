package com.arnold.core.netty.processor;

import lombok.Data;

@Data
public class test {

    private String name = "test";

    private static class instanceHolder {
        public static test instance = new test();
        static {
            instance.setName("dddd");
        }
    }

    public static test getInstance(){
        return instanceHolder.instance;
    }


    public static void main(String[] args) {
        test instance = test.getInstance();
        System.out.println(instance.getName());
    }
}
