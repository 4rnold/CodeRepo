package com.heima.modules.po;

import com.heima.commons.domin.po.PO;
import com.heima.commons.domin.vo.VO;
import com.heima.modules.vo.VehicleVO;

import java.io.Serializable;
import java.util.Date;

public class VehiclePO implements Serializable, PO {
    /**
     * 主键
     */
    private String id;

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
     * 购车日期
     */
    private Date purchaseDate;

    /**
     * 所属人手机号码
     */
    private String phone;

    /**
     * 认证状态 未认证：0
     * 认证成功：1
     * 认证失败：2
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
     * t_vehicle
     */
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        return VehicleVO.class;
    }
}