package com.arnold.Test;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.openjdk.jol.info.ClassLayout;

public class Test {

    public static void main(String[] args) {
        String a = "呵abcd";
        System.out.print(ClassLayout.parseClass(String.class));
        System.out.println(ObjectSizeCalculator.getObjectSize(a));
    }
}
