package com.tangyh.lamp.activiti.dto.activiti;

import com.tangyh.lamp.authority.entity.auth.User;
import com.tangyh.basic.annotation.injection.InjectionField;
import com.tangyh.basic.model.RemoteData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 活动任务返回实体
 *
 * @author wz
 * @date 2020-08-07
 */
@Data
@NoArgsConstructor
@ApiModel(value = "TaskResDTO", description = "活动任务返回实体")
public class TaskResDTO {
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    protected String id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    protected String name;

    /**
     * 审批人id
     */
    @ApiModelProperty(value = "审批人id")
    protected String assignee;

    /**
     * 审批人
     */
    @ApiModelProperty(value = "审批人")
    protected User cuser;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    protected String tenantId;

    /**
     * 是否挂起
     */
    @ApiModelProperty(value = "是否挂起")
    protected Boolean isSuspended;

    /**
     * 对应定义key
     */
    @ApiModelProperty(value = "对应定义key")
    protected String taskDefKey;

    /**
     * 对应流程实例
     */
    @ApiModelProperty(value = "对应流程实例")
    @InjectionField(api = "myProcessInstantService", method = "findProInst")
    protected RemoteData<String, ProcessInstanceResDTO> inst;

}
