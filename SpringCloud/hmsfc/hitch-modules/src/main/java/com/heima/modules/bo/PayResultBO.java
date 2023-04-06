package com.heima.modules.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付对象
 * {nonce_str=fvMGIlLauUPNCtws, code_url=weixin://wxpay/bizpayurl?pr=I5sd2rc, appid=wx8397f8696b538317,sign=48B2938F70EDADC9CC235249BC085FD1D83456F67C46601FFD23B5AFBDA502D0,trade_type=NATIVE, return_msg=OK, result_code=SUCCESS, mch_id=1473426802,return_code=SUCCESS, prepay_id=wx17193859685440d561c4cef01259098400}
 */
public class PayResultBO {

    public PayResultBO() {
    }


    public PayResultBO(Map<String, String> resultMap) {
        setOutTradeNo(resultMap.get("out_trade_no"));
        setTotalFee(resultMap.get("total_fee"));
        setPayInfo(resultMap.get("trade_state_desc"));
        setNonceStr(resultMap.get("nonce_str"));
        setCodeUrl(resultMap.get("code_url"));
        setAppid(resultMap.get("appid"));
        setReturnMsg(resultMap.get("return_msg"));
        setResultCode(resultMap.get("result_code"));
        setMchId(resultMap.get("mch_id"));
        setReturnCode(resultMap.get("return_code"));
        setErrorMsg(resultMap.get("err_code_des"));
        setPrepayId(resultMap.get("prepay_id"));
    }

    /**
     * 总金额
     */
    private String totalFee;
    /**
     * 支付订单号
     */
    private String outTradeNo;

    private String nonceStr;

    private String codeUrl;

    private String appid;

    private String returnMsg;

    private String resultCode;

    private String mchId;

    private String returnCode;

    private String payInfo;

    private String errorMsg;

    private String prepayId;


    public boolean isSuccess() {
        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode) && "支付成功".equals(payInfo)) {
            return true;
        }
        return false;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
}
