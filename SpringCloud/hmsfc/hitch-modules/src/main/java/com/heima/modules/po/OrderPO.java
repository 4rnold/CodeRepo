package com.heima.modules.po;

import com.heima.commons.domin.po.PO;
import com.heima.modules.vo.OrderVO;

import java.io.Serializable;
import java.util.Date;

public class OrderPO implements Serializable, PO {
    /**
     * 主键
     */
    private String id;

    /**
     * 乘客ID
     */
    private String passengerId;

    /**
     * 乘客行程ID
     */
    private String passengerStrokeId;

    /**
     * 司机ID
     */
    private String driverId;

    /**
     * 司机行程ID
     */
    private String driverStrokeId;

    /**
     * 距离 单位米
     */
    private Integer distance;

    /**
     * 预计时长 单位秒
     */
    private Integer estimatedTime;

    /**
     * 价格 单位元
     */
    private Float cost;


    /**
     * 未支付：0
     * 已支付：1
     * 未确认：2
     * 订单状态
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerStrokeId() {
        return passengerStrokeId;
    }

    public void setPassengerStrokeId(String passengerStrokeId) {
        this.passengerStrokeId = passengerStrokeId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverStrokeId() {
        return driverStrokeId;
    }

    public void setDriverStrokeId(String driverStrokeId) {
        this.driverStrokeId = driverStrokeId;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
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
        return OrderVO.class;
    }
}