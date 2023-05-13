package com.crazymakercircle.netty.protocol;

import com.crazymakercircle.util.JsonUtil;
import com.crazymakercircle.util.RandomUtil;
import lombok.Data;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Data
public class JsonMsg {

    //devId Field(域)
    private int id;
    //content Field(域)
    private String content = "疯狂创客圈-Java高并发社群";

    //在通用方法中，使用阿里FastJson转成Java对象

    public static JsonMsg parseFromJson(String json) {

        return JsonUtil.jsonToPojo(json, JsonMsg.class);
    }

    //在通用方法中，使用谷歌Gson转成字符串
    public String convertToJson() {
        return JsonUtil.pojoToJson(this);
    }
    public JsonMsg(int id) {
        this.id = id;
    }

    public JsonMsg() {
        this.id = RandomUtil.randInMod(100);
    }

}
