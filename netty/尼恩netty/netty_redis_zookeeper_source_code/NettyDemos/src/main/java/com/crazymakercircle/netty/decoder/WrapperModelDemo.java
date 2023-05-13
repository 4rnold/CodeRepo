package com.crazymakercircle.netty.decoder;

import org.junit.Test;

public class WrapperModelDemo {

    // Pojo -- 被包装的类型
    static class Pojo implements Sth {
        public void a() {
            System.out.println("a");
        }

        public void b() {
            System.out.println("b");
        }

        public void c() {
            System.out.println("c");
        }

        public void X() {
            System.out.println("ALL - X");
        }
    }

// Wrapper模式

    static class PojoWrapper implements Sth {
        private Sth inner;

        protected PojoWrapper() {

        }

        public void setInner(Sth inner) {
            this.inner = inner;
        }

        @Override
        public void a() {
            System.out.println("PojoWrapper - a");
            inner.a();
        }

        @Override
        public void b() {
            System.out.println("PojoWrapper - a");
            inner.b();
        }

        @Override
        public void c() {
            throw new UnsupportedOperationException("... c  ");
        }

        @Override
        public void X() {
            throw new UnsupportedOperationException("... X");
        }
    }


    @Test
    public void testWrapper() throws Exception {
        Sth pojo = new Pojo();
        // a
        // b
        // c
        pojo.a();
        pojo.b();
        pojo.c();

        PojoWrapper pojoWrapper = new PojoWrapper();
        pojoWrapper.setInner(pojo);


        // 可以尝试注释掉上面的某一行代码, 查看输出结果
        pojoWrapper.a();
        pojoWrapper.b();
        pojoWrapper.c();
    }

}
