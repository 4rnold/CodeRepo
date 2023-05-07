package com.tangyh.lamp.activiti.dto.activiti;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程实例查询入参
 *
 * @author wz
 * @date 2020-08-07
 */
@Data
@ApiModel(value = "InstantSelectReqDTO", description = "流程实例查询入参")
public class InstantSelectReqDTO {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * key
     */
    @ApiModelProperty(value = "key")
    private String key;
    /**
     * 用户id(后端权限查询，传值无效)
     */
    @ApiModelProperty(value = "用户id(后端权限查询，传值无效)")
    private String userId;
}
