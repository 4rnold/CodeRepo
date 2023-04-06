package com.heima.payment.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信支付配置属性类
 */
@ConfigurationProperties(prefix = "wxpay")
public class WXProperties {

    private String appid;

    private String mchid;

    private String key;

    private String notifyUrl;

    private String domain;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
