package com.heima.modules.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.modules.po.PaymentPO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

public class PaymentVO implements VO {
    /**
     * 主键
     */
    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    /**
     * 订单号
     */
    @NotEmpty(message = "订单号ID不能为空", groups = {Group.Create.class})
    private String orderId;

    /**
     * 预支付ID
     */
    private String prepayId;

    /**
     * 微信支付支付二维码地址
     */
    private String codeUrl;

    /**
     * 金额
     */
    @Min(value = 0, message = "支付金额必须大于0")
    private Float amount;

    /**
     * 支付渠道 支付宝：1微信：2
     */
    @InitialResolver(resolver = InitialResolverType.DEF_VALUE, groups = {Group.Create.class}, def = "2")
    private Integer channel;

    /**
     * 交易订单号
     */
    private String transactionOrderNum;

    /**
     * 支付信息
     */
    private String payInfo;

    /**
     * 支付状态 未支付：0
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
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Create.class, Group.Select.class})
    private String createdBy;

    /**
     * 创建时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Create.class})
    private Date createdTime;

    /**
     * 更新人
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Update.class})
    private String updatedBy;

    /**
     * 更新时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Update.class})
    private Date updatedTime;

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

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
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

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
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

    @JsonIgnore
    @Override
    public Class getPO() {
        return PaymentPO.class;
    }
}