package com.heima.modules.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.modules.po.OrderPO;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

public class OrderVO implements VO {
    /**
     * 主键
     */
    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
    private String id;
    /**
     * 用户角色
     */
    private Integer role;

    /**
     * 当前登陆人ID
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Select.class})
    private String currentUserId;

    /**
     * 乘客ID
     */
    private String passengerId;

    /**
     * 乘客行程ID
     */
    @NotEmpty(message = "乘客行程ID不能为空", groups = {Group.Create.class})
    private String passengerStrokeId;
    /**
     * 乘客行程状态
     */
    private Integer passengerStrokeStatus;

    /**
     * 司机ID
     */
    private String driverId;

    /**
     * 司机行程ID
     */
    @NotEmpty(message = "司机行程ID不能为空", groups = {Group.Create.class})
    private String driverStrokeId;
    /**
     * 司机行程状态
     */
    private Integer driverStrokeStatus;

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
     * 司机姓名
     */
    private String passengerUseralias;
    /**
     * 司机头像
     */

    private String passengerAvatar;
    /**
     * 司机电话
     */
    private String passengerPhone;

    /**
     * 司机姓名
     */
    private String driverUseralias;
    /**
     * 司机头像
     */

    private String driverAvatar;
    /**
     * 司机电话
     */
    private String driverPhone;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车牌前部照片
     */
    private String carFrontPhoto;

    /**
     * 车牌背部照片
     */
    private String carBackPhoto;

    /**
     * 车牌侧部照片
     */
    private String carSidePhoto;


    /**
     * 起点时间
     */
    private Date passengerStartDate;

    /**
     * 终点时间
     */
    private Date passengerEndDate;


    /**
     * 起点地址
     */
    private String passengerStartAddr;

    /**
     * 终点地址
     */
    private String passengerEndAddr;


    /**
     * 起点时间
     */
    private Date driverStartDate;

    /**
     * 终点时间
     */
    private Date driverEndDate;

    /**
     * 起点地址
     */
    private String driverStartAddr;

    /**
     * 终点地址
     */
    private String driverEndAddr;

    /**
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
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

    public Integer getPassengerStrokeStatus() {
        return passengerStrokeStatus;
    }

    public void setPassengerStrokeStatus(Integer passengerStrokeStatus) {
        this.passengerStrokeStatus = passengerStrokeStatus;
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

    public Integer getDriverStrokeStatus() {
        return driverStrokeStatus;
    }

    public void setDriverStrokeStatus(Integer driverStrokeStatus) {
        this.driverStrokeStatus = driverStrokeStatus;
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

    public String getPassengerUseralias() {
        return passengerUseralias;
    }

    public void setPassengerUseralias(String passengerUseralias) {
        this.passengerUseralias = passengerUseralias;
    }

    public String getPassengerAvatar() {
        return passengerAvatar;
    }

    public void setPassengerAvatar(String passengerAvatar) {
        this.passengerAvatar = passengerAvatar;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    public String getDriverUseralias() {
        return driverUseralias;
    }

    public void setDriverUseralias(String driverUseralias) {
        this.driverUseralias = driverUseralias;
    }

    public String getDriverAvatar() {
        return driverAvatar;
    }

    public void setDriverAvatar(String driverAvatar) {
        this.driverAvatar = driverAvatar;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarFrontPhoto() {
        return carFrontPhoto;
    }

    public void setCarFrontPhoto(String carFrontPhoto) {
        this.carFrontPhoto = carFrontPhoto;
    }

    public String getCarBackPhoto() {
        return carBackPhoto;
    }

    public void setCarBackPhoto(String carBackPhoto) {
        this.carBackPhoto = carBackPhoto;
    }

    public String getCarSidePhoto() {
        return carSidePhoto;
    }

    public void setCarSidePhoto(String carSidePhoto) {
        this.carSidePhoto = carSidePhoto;
    }

    public Date getPassengerStartDate() {
        return passengerStartDate;
    }

    public void setPassengerStartDate(Date passengerStartDate) {
        this.passengerStartDate = passengerStartDate;
    }

    public Date getPassengerEndDate() {
        return passengerEndDate;
    }

    public void setPassengerEndDate(Date passengerEndDate) {
        this.passengerEndDate = passengerEndDate;
    }

    public String getPassengerStartAddr() {
        return passengerStartAddr;
    }

    public void setPassengerStartAddr(String passengerStartAddr) {
        this.passengerStartAddr = passengerStartAddr;
    }

    public String getPassengerEndAddr() {
        return passengerEndAddr;
    }

    public void setPassengerEndAddr(String passengerEndAddr) {
        this.passengerEndAddr = passengerEndAddr;
    }

    public Date getDriverStartDate() {
        return driverStartDate;
    }

    public void setDriverStartDate(Date driverStartDate) {
        this.driverStartDate = driverStartDate;
    }

    public Date getDriverEndDate() {
        return driverEndDate;
    }

    public void setDriverEndDate(Date driverEndDate) {
        this.driverEndDate = driverEndDate;
    }

    public String getDriverStartAddr() {
        return driverStartAddr;
    }

    public void setDriverStartAddr(String driverStartAddr) {
        this.driverStartAddr = driverStartAddr;
    }

    public String getDriverEndAddr() {
        return driverEndAddr;
    }

    public void setDriverEndAddr(String driverEndAddr) {
        this.driverEndAddr = driverEndAddr;
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
    public Class getPO() {
        return OrderPO.class;
    }
}