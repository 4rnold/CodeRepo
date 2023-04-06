package com.heima.modules.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付对象
 */
public class WXPayBO {

    public WXPayBO() {
    }

    public WXPayBO(String outTradeNo, String totalFee, String body, String spbillCreateIp) {
        this.outTradeNo = outTradeNo;
        this.totalFee = totalFee;
        this.body = body;
        this.spbillCreateIp = spbillCreateIp;

    }

    /**
     * 总金额
     */
    private String totalFee;
    //支付内容
    private String body;
    //订单号
    private String outTradeNo;
    /**
     * 终端IP
     */
    private String spbillCreateIp;
    /**
     * 交易类型
     */
    private String tradeType = "NATIVE";

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Map<String, String> toMap() {
        Map<String, String> wxMap = new HashMap();
        wxMap.put("body", getBody());
        wxMap.put("out_trade_no", getOutTradeNo());
        wxMap.put("total_fee", getTotalFee());
        wxMap.put("spbill_create_ip", getSpbillCreateIp());
        wxMap.put("trade_type", getTradeType());
        return wxMap;
    }
}
