package com.heima.commons.entity;

import com.alibaba.fastjson.JSON;
import com.heima.commons.constant.HtichConstants;
import com.heima.commons.utils.CommonsUtils;
import com.heima.commons.utils.SnowflakeIdWorker;
import com.heima.commons.utils.reflect.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;

/**
 * Session的上下文
 */
public class SessionContext {
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    public SessionContext(Object data, String accountId, Map<String, List<String>> headerMap) {
        setSessionID(generateSessionId());
        setAccountID(accountId);
        setUsername(accountId);
        setUseralias(accountId);
        setClientIP(CommonsUtils.getHeaderValues(headerMap, "clientIP"));
        setBrowserVersion(CommonsUtils.getHeaderValues(headerMap, "browserVersion"));
        setData(data);
        if (null != data) {
            setDataType(data.getClass().getName());
        }
        setSerializeData(JSON.toJSONString(data));
        setCreateTime(new Date());
        setLastVisitTime(new Date());
    }

    /**
     * Map转Session
     *
     * @param map
     */
    public SessionContext(Map<String, String> map) {
        String sessionId = map.get("sessionID");
        if (StringUtils.isEmpty(sessionId)) {
            throw new IllegalArgumentException("sessionId不能为空");
        }
        setSessionID(sessionId);
        setAccountID(map.get("accountID"));
        setUsername(map.get("username"));
        setUseralias(map.get("useralias"));
        setClientIP(map.get("clientIP"));
        setBrowserVersion(map.get("browserVersion"));
        setCreateTime(new Date(Long.parseLong(map.get("createTime"))));
        setLastVisitTime(new Date(Long.parseLong(map.get("lastVisitTime"))));
        setSerializeData(map.get("serializeData"));
        String dataType = map.get("dataType");
        if (StringUtils.isNotEmpty(dataType)) {
            setDataType(dataType);
        }
    }

    public Map<String, String> toHash() {
        Map<String, String> map = new HashMap<>();
        map.put("sessionID", getSessionID());
        map.put("accountID", getAccountID());
        map.put("username", getUsername());
        map.put("useralias", getUseralias());
        map.put("clientIP", getClientIP());
        map.put("browserVersion", getBrowserVersion());
        map.put("createTime", String.valueOf(getCreateTime().getTime()));
        map.put("lastVisitTime", String.valueOf(getLastVisitTime().getTime()));
        map.put("serializeData", getSerializeData());
        map.put("dataType", getDataType());
        return map;
    }


    /**
     * sessionID
     */
    private String sessionID;

    private String accountID;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户姓名
     */
    private String useralias;
    /**
     * 客户端IP
     */
    private String clientIP;
    /**
     * 浏览器版本
     */
    private String browserVersion;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后访问时间
     */
    private Date lastVisitTime;

    /**
     * 序列化后的数据
     */
    private String serializeData;

    /**
     * 持久化的数据
     */
    private Object data;
    /**
     * 数据类型
     */
    private String dataType;


    private String generateSessionId() {
        return HtichConstants.SESSION_TOKEN_PREFIX + String.valueOf(idWorker.nextId());
    }

    public String getAccountTokenKey() {
        return getAccountTokenKey(getAccountID());
    }

    public static String getAccountTokenKey(String accountId) {
        return HtichConstants.ACCOUNT_TOKEN_PREFIX + accountId;
    }


    public String getSessionID() {
        return sessionID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseralias() {
        return useralias;
    }

    public void setUseralias(String useralias) {
        this.useralias = useralias;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public String getSerializeData() {
        return serializeData;
    }

    public void setSerializeData(String serializeData) {
        this.serializeData = serializeData;
    }

    public Object getData() {
        if (data == null && null != dataType && StringUtils.isNotEmpty(serializeData)) {
            Class clazz = ReflectUtils.classForName(dataType);
            if (null != clazz) {
                data = JSON.parseObject(serializeData, clazz);
            }
        }
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
