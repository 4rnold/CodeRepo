package com.heima.modules.po;

import com.heima.commons.domin.po.PO;
import com.heima.modules.vo.PaymentVO;

import java.io.Serializable;
import java.util.Date;

public class PaymentPO implements Serializable, PO {
    /**
     * 主键
     */
    private String id;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 预支付ID
     */
    private String prepayId;

    /**
     * 支付信息
     */
    private String payInfo;

    /**
     * 金额
     */
    private Float amount;

    /**
     * 支付渠道 支付宝：1微信：2
     */
    private Integer channel;

    /**
     * 交易订单号
     */
    private String transactionOrderNum;

    /**
     * 支付状态
     * 未支付：0
     * 已支付：1
     * 未确认：2
     */
    private Integer status;

    /**
     * 乐观锁
     */
    private Integer revision;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * t_payment
     */
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getTransactionOrderNum() {
        return transactionOrderNum;
    }

    public void setTransactionOrderNum(String transactionOrderNum) {
        this.transactionOrderNum = transactionOrderNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public Class getVO() {
        return PaymentVO.class;
    }
}