package com.heima.modules.po;

import com.heima.commons.domin.po.PO;
import com.heima.modules.vo.StrokeVO;

import java.io.Serializable;
import java.util.Date;

public class StrokePO implements Serializable, PO {
    /**
     * 主键
     */
    private String id;

    /**
     * 发布人ID
     */
    private String publisherId;

    /**
     * 发布人角色 乘客：0
     * 司机：1
     */
    private Integer role;

    /**
     * 起点GEO 经度
     */
    private String startGeoLng;

    /**
     * 起点GEO 纬度
     */
    private String startGeoLat;

    /**
     * 终点GEO 经度
     */
    private String endGeoLng;

    /**
     * 终点GEO 纬度
     */
    private String endGeoLat;

    /**
     * 起点地址
     */
    private String startAddr;

    /**
     * 终点地址
     */
    private String endAddr;

    /**
     * 数量 对于乘客来说是-同行人数
     * 对于司机来说是-空座数
     */
    private Integer quantity;

    /**
     * 出发时间
     */
    private Date departureTime;

    /**
     * 快速确认 是否开启快速确认
     * 对于乘客就是-闪电确认
     * 对于司机就是-自动接单
     */
    private Integer quickConfirm;

    /**
     * 行程状态 未接单：0
     * 已接单：1
     * 已出发：2
     * 行程中：3
     * 已结束：4
     * 已超时：5
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
     * t_stroke
     */
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId == null ? null : publisherId.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getStartGeoLng() {
        return startGeoLng;
    }

    public void setStartGeoLng(String startGeoLng) {
        this.startGeoLng = startGeoLng;
    }

    public String getStartGeoLat() {
        return startGeoLat;
    }

    public void setStartGeoLat(String startGeoLat) {
        this.startGeoLat = startGeoLat;
    }

    public String getEndGeoLng() {
        return endGeoLng;
    }

    public void setEndGeoLng(String endGeoLng) {
        this.endGeoLng = endGeoLng;
    }

    public String getEndGeoLat() {
        return endGeoLat;
    }

    public void setEndGeoLat(String endGeoLat) {
        this.endGeoLat = endGeoLat;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr == null ? null : startAddr.trim();
    }

    public String getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(String endAddr) {
        this.endAddr = endAddr == null ? null : endAddr.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getQuickConfirm() {
        return quickConfirm;
    }

    public void setQuickConfirm(Integer quickConfirm) {
        this.quickConfirm = quickConfirm;
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
        this.createdBy = createdBy == null ? null : createdBy.trim();
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
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public Class getVO() {
        return StrokeVO.class;
    }
}