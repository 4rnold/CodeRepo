package com.heima.modules.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.heima.commons.constant.HtichConstants;
import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.modules.po.AccountPO;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class AccountVO implements VO {

    /**
     * 主键
     */
    @InitialResolver(resolver = InitialResolverType.GEN_SNOWFLAKE_ID, groups = {Group.Create.class})
    @NotEmpty(message = "ID不能为空", groups = {Group.Update.class})
    private String id;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空", groups = {Group.Create.class, Group.Select.class})
    private String username;
    /**
     * 用户姓名
     */
    private String useralias;

    /**
     * 当前登录用户
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.All.class})
    private String currentUserId;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空", groups = {Group.Create.class, Group.Select.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 新密码 用户密码修改用
     */
    private String newPassword;

    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空", groups = {Group.Create.class})
    private String phone;

    /**
     * 角色 乘客：1司机：2
     */
    private Integer role;

    /**
     * 用户头像
     */
    @InitialResolver(resolver = InitialResolverType.DEF_VALUE, groups = {Group.Create.class}, def = HtichConstants.ACCOUNT_DEFAULT_AVATAR)
    private String avatar;


    /**
     * 状态 禁用：0
     * 正常：1
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
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Update.class})
    private String updatedBy;

    /**
     * 更新时间
     */
    @InitialResolver(resolver = InitialResolverType.CURRENT_DATE, groups = {Group.Update.class})
    private Date updatedTime;


    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseralias() {
        return useralias;
    }

    public void setUseralias(String useralias) {
        this.useralias = useralias;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonIgnore
    @Override
    public Class getPO() {
        return AccountPO.class;
    }

}
