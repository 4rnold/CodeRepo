package com.itheima.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Class: JSONTools
 * @Package com.itheima.common
 * @Description:
 * @date 2020/3/27 15:40
 * @Company: http://www.itheima.com/
 */
public class JSONTools {
    public static boolean isJSON(String str) {
        boolean result = false;
        try {
            Object obj = JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }

        return result;

    }


    public static JSONObject getJSONObject(String jsonStr) {
        JSONObject jsonObject = null;
        try {
            Object object = JSON.parse(jsonStr);
            if (object instanceof JSONObject) {
                jsonObject = (JSONObject) object;

            }

        } catch (Exception e) {
            return null;
        }

        return jsonObject;
    }
}
