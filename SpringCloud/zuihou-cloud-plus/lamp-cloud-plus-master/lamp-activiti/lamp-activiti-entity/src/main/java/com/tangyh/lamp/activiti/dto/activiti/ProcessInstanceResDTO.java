package com.tangyh.lamp.activiti.dto.activiti;

import com.tangyh.basic.model.RemoteData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程实例返回实体
 *
 * @author wz
 * @date 2020-08-07
 */
@Data
@ApiModel(value = "ProcessInstanceResDTO", description = "流程实例返回实体")
public class ProcessInstanceResDTO {
    /**
     * 实例id
     */
    @ApiModelProperty(value = "实例id")
    private String id;

    /**
     * 实例id
     */
    @ApiModelProperty(value = "实例id")
    private String procInstId;

    /**
     * 业务key
     */
    @ApiModelProperty(value = "业务key")
    private String businessKey;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    /**
     * 持续时间
     */
    @ApiModelProperty(value = "持续时间")
    private Long duration;

    /**
     * 开始用户
     */
    @ApiModelProperty(value = "开始用户")
    private RemoteData<Long, String> startUser;

    /**
     * 活动开始id
     */
    @ApiModelProperty(value = "活动开始id")
    private String startActId;

    /**
     * 活动结束id
     */
    @ApiModelProperty(value = "活动结束id")
    private String endActId;

    /**
     * 父流程id
     */
    @ApiModelProperty(value = "父流程id")
    private String superProcessInstanceId;

    /**
     * 删除原因
     */
    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;

    /**
     * 流程定义key
     */
    @ApiModelProperty(value = "流程定义key")
    private String processDefinitionKey;

    /**
     * 流程定义名称
     */
    @ApiModelProperty(value = "流程定义名称")
    private String processDefinitionName;

    /**
     * 流程部署id
     */
    @ApiModelProperty(value = "流程部署id")
    private String deploymentId;

    /**
     * 流程部署key
     */
    @ApiModelProperty(value = "流程部署key")
    private String rootProcessInstanceId;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String suspendState;

    /**
     * 变量
     */
    @ApiModelProperty(value = "变量")
    private Map<String, Object> value = new HashMap<>(16);

    @Builder

    public ProcessInstanceResDTO(String id, String procInstId, String businessKey, LocalDateTime startTime, LocalDateTime endTime, Long duration, RemoteData<Long, String> startUser, String startActId, String endActId, String superProcessInstanceId, String deleteReason, String tenantId, String name, String processDefinitionId, String processDefinitionKey, String processDefinitionName, String deploymentId, String rootProcessInstanceId, String suspendState, Map<String, Object> value) {
        this.id = id;
        this.procInstId = procInstId;
        this.businessKey = businessKey;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.startUser = startUser;
        this.startActId = startActId;
        this.endActId = endActId;
        this.superProcessInstanceId = superProcessInstanceId;
        this.deleteReason = deleteReason;
        this.tenantId = tenantId;
        this.name = name;
        this.processDefinitionId = processDefinitionId;
        this.processDefinitionKey = processDefinitionKey;
        this.processDefinitionName = processDefinitionName;
        this.deploymentId = deploymentId;
        this.rootProcessInstanceId = rootProcessInstanceId;
        this.suspendState = suspendState;
        this.value = value;
    }
}
