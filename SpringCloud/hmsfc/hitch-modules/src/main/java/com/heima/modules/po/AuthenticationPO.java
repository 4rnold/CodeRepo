package com.heima.modules.po;

import com.heima.commons.domin.po.PO;
import com.heima.modules.vo.AuthenticationVO;

import java.io.Serializable;
import java.util.Date;

public class AuthenticationPO implements Serializable, PO {
    /**
     * 主键
     */
    private String id;

    /**
     * 用户姓名
     */
    private String useralias;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 年龄
     */
    private String birth;

    /**
     * 个人照片
     */
    private String personalPhoto;

    /**
     * 身份证号码
     */
    private String cardId;

    /**
     * 身份证正面照片
     */
    private String cardIdFrontPhoto;

    /**
     * 身份证背面照片
     */
    private String cardIdBackPhoto;

    /**
     * 认证状态 未认证：0认证成功：1认证失败：2
     */
    private String status;

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
     * t_authentication
     */
    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUseralias() {
        return useralias;
    }

    public void setUseralias(String useralias) {
        this.useralias = useralias;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPersonalPhoto() {
        return personalPhoto;
    }

    public void setPersonalPhoto(String personalPhoto) {
        this.personalPhoto = personalPhoto;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardIdFrontPhoto() {
        return cardIdFrontPhoto;
    }

    public void setCardIdFrontPhoto(String cardIdFrontPhoto) {
        this.cardIdFrontPhoto = cardIdFrontPhoto;
    }

    public String getCardIdBackPhoto() {
        return cardIdBackPhoto;
    }

    public void setCardIdBackPhoto(String cardIdBackPhoto) {
        this.cardIdBackPhoto = cardIdBackPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        return AuthenticationVO.class;
    }
}