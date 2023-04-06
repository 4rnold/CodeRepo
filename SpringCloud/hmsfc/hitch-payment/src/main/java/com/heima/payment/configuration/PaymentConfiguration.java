package com.heima.payment.configuration;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * 支付配置类
 */
@Configuration
@EnableConfigurationProperties(WXProperties.class)
public class PaymentConfiguration {
    //声明微信支付sdk
    @Bean
    public WXPay wxPay(WXProperties properties) throws Exception {
        WXPayConfig wxPayConfig = getWXPayConfig(properties);
        return new WXPay(wxPayConfig, wxPayConfig.getNotifyUrl());
    }

    /**
     * 生成微信支付配置
     *
     * @param properties
     * @return
     */
    private WXPayConfig getWXPayConfig(WXProperties properties) {
        return new WXPayConfig() {
            //appid
            @Override
            public String getAppID() {
                return properties.getAppid();
            }

            //mchid
            @Override
            public String getMchID() {
                return properties.getMchid();
            }

            //微信支付的key
            @Override
            public String getKey() {
                return properties.getKey();
            }

            //异步通知地址
            @Override
            public String getNotifyUrl() {
                return properties.getNotifyUrl();
            }

            //是否是沙箱模式
            @Override
            public boolean useSandbox() {
                return false;
            }

            //证书配置
            @Override
            public InputStream getCertStream() {
                return null;
            }

            //微信支付域配置
            public IWXPayDomain getWXPayDomain() {
                return new IWXPayDomain() {
                    public void report(String domain, long elapsedTimeMillis,
                                       Exception ex) {
                    }

                    public DomainInfo getDomain(WXPayConfig config) {
                        return new DomainInfo(properties.getDomain(), true);
                    }
                };
            }
        };
    }
}
