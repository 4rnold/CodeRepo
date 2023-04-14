package com.itheima.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/***
 *
 *
 ****/
public class ItemTest {


    /***
     * 启动服务
     */
    @Test
    public void testItem() throws IOException {
        ApplicationContext act = new ClassPathXmlApplicationContext("spring/applicationContext.xml");

        //阻塞线程
        System.in.read();
    }
}
