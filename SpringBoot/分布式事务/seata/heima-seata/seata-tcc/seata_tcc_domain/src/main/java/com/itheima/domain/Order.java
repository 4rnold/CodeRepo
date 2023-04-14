package com.itheima.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 */
public class Order implements Serializable {

    //订单ID
    private String id;

    //创建时间
    private Date createTime;

    //订单编号
    private String number;

    //订单状态
    private Integer status;

    //商品id
    private String productId;

    //付款金额
    private BigDecimal totalAmount;

    //购买数量
    private Integer count;

    //购买人
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
