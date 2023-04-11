package com.itheima.domain;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 记录轨迹的实体
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class ConfigLocos implements Serializable {

    @Id
    private String id;
    private String configInfoId;
    private String envName;
    private String projectName;
    private String clusterNumber;
    private String serviceName;
    private String configDetail;
    private String userId;
    private String projectGroup;
    private Long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigInfoId() {
        return configInfoId;
    }

    public void setConfigInfoId(String configInfoId) {
        this.configInfoId = configInfoId;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
