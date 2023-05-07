package com.tangyh.lamp.activiti.domain.activiti;

import com.tangyh.basic.base.entity.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 流程定义关联DO
 *
 * @author wz
 * @date 2020-08-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessDefinitionDO extends Entity<String> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "流程名称")
    private String name;

    @ApiModelProperty(name = "流程KEY")
    private String key;

    @ApiModelProperty(name = "流程版本")
    private Integer version;

    @ApiModelProperty(name = "所属分类")
    private String category;

    @ApiModelProperty(name = "流程描述")
    private String description;

    @ApiModelProperty(name = "部署id")
    private String deploymentId;

    @ApiModelProperty(name = "部署名称")
    private String deploymentName;

    @ApiModelProperty(name = "部署时间")
    private Date deploymentTime;

    @ApiModelProperty(name = "流程图")
    private String diagramResourceName;

    @ApiModelProperty(name = "流程定义")
    private String resourceName;

    @ApiModelProperty(name = "激活状态 1 激活 2 挂起")
    private String suspendState;

    @ApiModelProperty(name = "激活状态名")
    private String suspendStateName;

    @ApiModelProperty(name = "租户id")
    private String tenantId;
}
