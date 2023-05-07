package com.tangyh.lamp.activiti.dto.activiti;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 流程实例保存入参
 *
 * @author wz
 * @date 2020-08-07
 */
@Data
@ApiModel(value = "InstantSelectSaveDTO", description = "流程实例保存入参")
public class InstantSelectSaveDTO {
    /**
     * 流程key
     */
    @ApiModelProperty(value = "流程key")
    private String key;

    /**
     * 业务key
     */
    @ApiModelProperty(value = "业务key")
    private String bussKey;

    /**
     * 流程变量
     */
    @ApiModelProperty(value = "流程变量")
    private Map<String, Object> variables;
}
