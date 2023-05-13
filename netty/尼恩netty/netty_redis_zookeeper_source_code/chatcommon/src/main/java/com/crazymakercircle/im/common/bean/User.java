package com.crazymakercircle.im.common.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class User implements Serializable {

    String uid;
    String devId;
    String token;
    String nickName;
    transient PLATTYPE platform;
    int intPlatFrom;

    public User() {
        nickName = "nickName";
        setPlatform(PLATTYPE.ANDROID);
    }

    // windows,mac,android, ios, web , other
    public enum PLATTYPE {
        WINDOWS, MAC, ANDROID, IOS, WEB, OTHER;
    }

    private String sessionId;

    @JSONField(serialize = false)
    public void setPlatform(PLATTYPE platform) {

        this.platform = platform;
        this.intPlatFrom = platform.ordinal();

    }

    @JSONField(serialize = false)
    public PLATTYPE getPlatform() {
        if(null==platform)
        {
            this.platform = PLATTYPE.values()[intPlatFrom];
        }
        return platform;
    }

    @JSONField(name = "intPlatFrom")
    public int getIntPlatFrom() {
        this.platform = PLATTYPE.values()[intPlatFrom];
        return intPlatFrom;
    }

    @JSONField(name = "intPlatFrom")
    public void setIntPlatFrom(int code) {
        this.intPlatFrom = code;
        this.platform = PLATTYPE.values()[code];
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + getUid() + '\'' +
                ", nickName='" + getNickName() + '\'' +
                ", platform=" + getPlatform() +
                ", intPlatFrom=" + getIntPlatFrom() +
                '}';
    }

    public static User fromMsg(ProtoMsg.LoginRequest info) {
        User user = new User();
        user.uid = new String(info.getUid());
        user.devId = new String(info.getDeviceId());
        user.token = new String(info.getToken());
        user.setIntPlatFrom(info.getPlatform());
        log.info("登录中: {}", user.toString());
        return user;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public static UserBuilder builder(){
        return new UserBuilder();
    }


    /**
     *   1、User类中创建一个静态内部类 Builder
     *
     *         2、Builder 类中，包含User类的全部属性
     *
     *         3、Builder 类中，每个属性创建赋值方法，并返回当前对象
     *
     *           4、Builder 类中，创建 build方法，返回User对象并赋值
     *
     *           5、User类中，创建静态builder方法，返回Builder对象
     */
    public static   class UserBuilder {
        private String devId;
        private String name ;
        private Integer platform;

        public UserBuilder devId(String id) {
            this.devId = id ;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name ;
            return this ;
        }

        public UserBuilder platform(Integer platform) {
            this.platform = platform ;
            return this;
        }


        public User build() {
            User user= new User();
            user.setDevId(devId);
            user.setNickName(name);
            user.setIntPlatFrom(platform);
            return user;
        }

    }


}
