package com.heima.commons.domin.vo.request;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class RequestVO {
    /**
     * 消息主体
     */
    private JSONArray body;
    /**
     * 数据类型
     */
    private String type;
    /**
     * 当前登录用户
     */
    private String loginAccountId;

    private List<Object> dataList = new ArrayList<>();


    /**
     * 校验参数
     *
     * @return
     */
   /* public boolean checkParameter() {
        if (StringUtils.isEmpty(type)) {
            return false;
        }
        return verifyParameter();
    }*/
    public void parseBodyData(Class clazz) {
        if (null == body) {
            return;
        }
        dataList = body.toJavaList(clazz);
    }

    public List<Object> getMultipleData() {
        return dataList;
    }

    public Object getData() {
        if (null != dataList && !dataList.isEmpty()) {
            return dataList.get(0);
        }
        return null;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONArray getBody() {
        return body;
    }

    public void setBody(JSONArray body) {
        this.body = body;
    }

    public void setDataList(List<Object> dataList) {
        this.dataList = dataList;
    }

    public String getLoginAccountId() {
        return loginAccountId;
    }

    public void setLoginAccountId(String loginAccountId) {
        this.loginAccountId = loginAccountId;
    }
}
