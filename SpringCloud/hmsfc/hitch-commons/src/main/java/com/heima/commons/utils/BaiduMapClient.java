package com.heima.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heima.commons.domin.bo.RoutePlanResultBO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaiduMapClient {
    private static final String API_URL = "https://api.map.baidu.com/routematrix/v2/driving";
    /*@Value(("${baidu.map.ak}"))*/
    private static final String ak = "EoHbxdtKC097db8DVk1Qq1LVe1Ip2Yx4";


    public static List<RoutePlanResultBO> pathPlanning(String origins, String destinations) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("ak", ak);
        reqMap.put("origins", origins);
        reqMap.put("destinations", destinations);
        String result = null;
        try {
            result = HttpClientUtils.doGet(API_URL, reqMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultAssemble(result);
    }

    public static List<RoutePlanResultBO> resultAssemble(String result) {
        List<RoutePlanResultBO> resultBOS = null;
        JSONObject jsonObject = (JSONObject) JSON.parse(result);
        if (null != jsonObject && jsonObject.getString("status").equals("0")) {
            JSONArray resultArray = jsonObject.getJSONArray("result");
            if (null != resultArray && !resultArray.isEmpty()) {
                resultBOS = resultArray.toJavaList(RoutePlanResultBO.class);
            }
        }
        return resultBOS;
    }

}
