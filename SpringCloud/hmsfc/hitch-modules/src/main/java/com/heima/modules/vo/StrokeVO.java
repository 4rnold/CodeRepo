package com.heima.modules.vo;

import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.modules.po.StrokePO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class StrokeVO implements VO {


    /**
     * 主键
     */
    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
    private String id;

    /**
     * 发布人ID
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Create.class, Group.Select.class})
    private String publisherId;
    /**
     *  获取司机行程ID
     */
    private String inviterTripId;
    /**
     * 乘客行程ID
     */
    private String inviteeTripId;

    /**
     * 人员姓名
     */
    private String useralias;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 发布人角色 乘客：1
     * 司机：2
     */
    @NotNull(message = "发布人角色不能为空", groups = {Group.Create.class, Group.Select.class})
    private Integer role;

    /**
     * 起点GEO
     */
    @NotNull(message = "起点经度坐标不能为空", groups = {Group.Create.class})
    private String startGeoLng;

    @NotNull(message = "起点维度坐标不能为空", groups = {Group.Create.class})
    private String startGeoLat;

    /**
     * 终点GEO
     */
    @NotNull(message = "终点经度坐标不能为空", groups = {Group.Create.class})
    private String endGeoLng;

    @NotNull(message = "终点维度坐标不能为空", groups = {Group.Create.class})
    private String endGeoLat;


    /**
     * 起点地址
     */
    @NotNull(message = "起点地址不能为空", groups = {Group.Create.class})
    private String startAddr;

    /**
     * 起点距离
     */
    private Float startDistance;

    /**
     * 终点地址
     */
    @NotEmpty(message = "终点地址不能为空", groups = {Group.Create.class})
    private String endAddr;

    /**
     * 终点距离
     */
    private Float endDistance;

    /**
     * 匹配度
     */
    private String suitability;

    /**
     * 数量 对于乘客来说是-同行人数
     * 对于司机来说是-空座数
     */
    private Integer quantity;

    /**
     * 出发时间
     */
    @NotNull(message = "出发时间不能为空", groups = {Group.Create.class})
    private Date departureTime;

    /**
     * 快速确认 是否开启快速确认
     * 对于乘客就是-闪电确认
     * 对于司机就是-自动接单
     */
    @NotNull(message = "快速确认状态不能为空", groups = {Group.Update.class})
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
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Create.class})
    private String createdBy;

    /**
     * 创建时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Create.class})
    private Date createdTime;

    /**
     * 更新人
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Create.class, Group.Update.class})
    private String updatedBy;

    /**
     * 更新时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Create.class, Group.Update.class})
    private Date updatedTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getUseralias() {
        return useralias;
    }

    public void setUseralias(String useralias) {
        this.useralias = useralias;
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
        this.startAddr = startAddr;
    }

    public Float getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(Float startDistance) {
        this.startDistance = startDistance;
    }

    public String getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(String endAddr) {
        this.endAddr = endAddr;
    }

    public Float getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(Float endDistance) {
        this.endDistance = endDistance;
    }


    public String getSuitability() {
        return suitability;
    }

    public void setSuitability(String suitability) {
        this.suitability = suitability;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getInviterTripId() {
        return inviterTripId;
    }

    public void setInviterTripId(String inviterTripId) {
        this.inviterTripId = inviterTripId;
    }

    public String getInviteeTripId() {
        return inviteeTripId;
    }

    public void setInviteeTripId(String inviteeTripId) {
        this.inviteeTripId = inviteeTripId;
    }


    @Override
    public Class getPO() {
        return StrokePO.class;
    }
}