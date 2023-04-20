package com.itheima.a23;

interface InterA {
    void f();
}

interface InterB {
    void f();
}

public class C extends ImplB implements InterA {
//    public void f() {
//// 实现接口InterA方法
//        System.out.println("c.f()");
//    }

    // 使用
    public static void main(String[] args) {
        C c = new C();
        InterA a = c; //C实现了A接口，可以直接向上转型
        a.f();
    }
}

class ImplB implements InterB {
    public void f() {
// 实现接口InterA方法
        System.out.println("B.f()");
    }
}