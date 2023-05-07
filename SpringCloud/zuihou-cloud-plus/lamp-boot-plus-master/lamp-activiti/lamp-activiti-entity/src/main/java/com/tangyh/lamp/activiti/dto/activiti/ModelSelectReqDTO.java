package com.tangyh.lamp.activiti.dto.activiti;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模型查询入参
 *
 * @author wz
 * @date 2020-08-07
 */
@Data
@ApiModel(value = "ModelSelectReqDTO", description = "模型查询入参")
public class ModelSelectReqDTO {
    /**
     * 模型名
     */
    @ApiModelProperty(value = "模型名")
    private String name;
    /**
     * 模型key
     */
    @ApiModelProperty(value = "模型key")
    private String key;
}
