package com.crazymakercircle.util;

import com.crazymakercircle.json.JsonContext;
import com.crazymakercircle.json.JsonStrategy;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {


    //Object对象转成JSON字符串后，进一步转成字节数组
    public static byte[] object2JsonBytes(Object obj) {

        //把对象转换成JSON

        String json = pojoToJson(obj);
        try {
            return json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //反向：字节数组，转成JSON字符串，转成Object对象
    public static <T> T jsonBytes2Object(byte[] bytes, Class<T> tClass) {
        //字节数组，转成JSON字符串
        try {
            String json = new String(bytes, "UTF-8");
            T t = jsonToPojo(json, tClass);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //序列化： pojo =》 json 字符串
    //使用策略模式 将 POJO 转成字符串
    public static String pojoToJson(Object obj) {
        //使用谷歌 Gson 将 POJO 转成字符串
        //String json = new Gson().toJson(obj);
        //String json = gson.toJson(obj);

        // client
        JsonStrategy strategy = JsonContext.getStrategy();

        String json = strategy.toJson(obj);
        return json;
    }

    //反序列化：json 字符串 =》  pojo
    //使用策略模式 将 字符串 转成POJO
    public static <T> T jsonToPojo(String json, Class<T> tClass) {
        //使用阿里 Fastjson 将字符串转成 POJO对象
//        T t = JSONObject.parseObject(json, tClass);
        JsonStrategy strategy = JsonContext.getStrategy();

        T t = strategy.fromJson(json, tClass);
        return t;
    }
    public static  <K, V> Map<K, V> jsonToMap(String json, Type type) {
        //使用阿里 Fastjson 将字符串转成 POJO对象
//        T t = JSONObject.parseObject(json, tClass);
        JsonStrategy strategy = JsonContext.getStrategy();

        Map<K, V> t = strategy.toMap(json,type);
        return t;
    }

}
