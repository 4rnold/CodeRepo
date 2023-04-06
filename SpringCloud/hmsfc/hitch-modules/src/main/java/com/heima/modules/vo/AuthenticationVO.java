package com.heima.modules.vo;

import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.modules.po.AuthenticationPO;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class AuthenticationVO implements VO {
    /**
     * 主键
     */
    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
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
    public Class getPO() {
        return AuthenticationPO.class;
    }
}