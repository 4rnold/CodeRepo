package com.itheima.domain;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Entity
@Table(name="config_info")
public class ConfigInfo implements Serializable {

    @Id
    private String id;
    @Column(name="env_name")
    private String envName;
    @Column(name="project_name")
    private String projectName;
    @Column(name="cluster_number")
    private String clusterNumber;
    @Column(name="service_name")
    private String serviceName;
    @Column(name="config_detail")
    private String configDetail;
    @Column(name="user_id")
    private String userId;
    @Column(name="project_group")
    private String projectGroup;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(String clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getConfigDetail() {
        return configDetail;
    }

    public void setConfigDetail(String configDetail) {
        this.configDetail = configDetail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(String projectGroup) {
        this.projectGroup = projectGroup;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ConfigInfo{" +
                "id='" + id + '\'' +
                ", envName='" + envName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", clusterNumber='" + clusterNumber + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", configDetail='" + configDetail + '\'' +
                ", userId='" + userId + '\'' +
                ", projectGroup='" + projectGroup + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
