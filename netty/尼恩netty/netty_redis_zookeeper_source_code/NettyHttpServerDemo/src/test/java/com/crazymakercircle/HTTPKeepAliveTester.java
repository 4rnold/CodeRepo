package com.crazymakercircle;

import com.crazymakercircle.util.HttpClientHelper;
import com.crazymakercircle.util.JsonUtil;
import com.crazymakercircle.util.JvmUtil;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HTTPKeepAliveTester {
    //HTTP echo 回显服务的地址，该服务部署在虚拟机192.168.233.128上
    private String url = "http://192.168.233.128:18899/";
    private ExecutorService pool = Executors.newFixedThreadPool(10);

    /**
     * 测试用例：使用JDK的 java.net.HttpURLConnection发起HTTP请求
     */
    @Test
    public void simpleGet() throws IOException, InterruptedException {
        /**
         * 提交的请求次数
         */
        int index = 1000000;
        while (--index > 0) {
            String target = url /*+ index*/;
            //使用固定20个线程的线程池发起请求
            pool.submit(() ->
            {
                //使用JDK的 java.net.HttpURLConnection发起HTTP请求
                String out = HttpClientHelper.jdkGet(target);
                System.out.println("out = " + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 测试用例：使用带连接池的Apache HttpClient提交的HTTP请求
     */
    @Test
    public void pooledGet() throws IOException, InterruptedException {
        int index = 1000000;
        while (--index > 0) {
            String target = url + index;
            //使用固定20个线程的线程池发起请求
            pool.submit(() ->
            {
                //使用Apache HttpClient提交的HTTP请求
                String out = HttpClientHelper.get(target);
                System.out.println("out = " + out);
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void continuePooledGet() throws IOException, InterruptedException {
        int index = 1000000;
        while (--index > 0) {
            String target = url + index;

            String out = HttpClientHelper.get(target);
            System.out.println("out = " + out);
            //睡眠1s
            Thread.sleep(1000);
        }

    }

    @Test
    public void mapJson() throws IOException, InterruptedException {


        Map<Integer, Long> map = new HashMap<>();

        map.put(1, 111L);
        map.put(3, 1114L);
        map.put(4, 111444L);

        String json = JsonUtil.pojoToJson(map);

        System.out.println(json);
        Type type = JvmUtil.getType(new HashMap<Integer, Long>() {
        });
        Map<Integer, Long> map2 = JsonUtil.jsonToMap(json, type);

        System.out.println("map2 = " + map2);

        Map<String, String> map3 = new HashMap<>();

        map3.put("疯狂创客圈", "java 高并发社群");
        map3.put("疯狂创客圈 ok", "java 高并发社群");
        json = JsonUtil.pojoToJson(map3);
        System.out.println(json);
        type = JvmUtil.getType(new HashMap<String, String>() {
        });
        Map<String, String> map4 = JsonUtil.jsonToMap(json, type);

        System.out.println("map4 = " + map4);

    }
}
