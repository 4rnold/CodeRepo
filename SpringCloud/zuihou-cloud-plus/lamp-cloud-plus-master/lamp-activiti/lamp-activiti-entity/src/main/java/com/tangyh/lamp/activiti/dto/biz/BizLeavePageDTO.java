package com.tangyh.lamp.activiti.dto.biz;

import com.tangyh.basic.base.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 实体类
 * 请假流程
 * </p>
 *
 * @author wz
 * @since 2020-08-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "BizLeavePageDTO", description = "请假流程")
public class BizLeavePageDTO extends Entity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假人员
     */
    @ApiModelProperty(value = "请假人员")
    @Length(max = 32, message = "请假人员长度不能超过32")
    private String name;
    /**
     * 请假时间
     */
    @ApiModelProperty(value = "请假时间")
    private LocalDateTime starttime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endtime;
    /**
     * 请假时长
     */
    @ApiModelProperty(value = "请假时长")
    @Length(max = 255, message = "请假时长长度不能超过255")
    private String whenlong;
    /**
     * 请假类型
     */
    @ApiModelProperty(value = "请假类型")
    @Length(max = 8, message = "请假类型长度不能超过8")
    private String type;
    /**
     * 请假事由
     */
    @ApiModelProperty(value = "请假事由")
    @Length(max = 255, message = "请假事由长度不能超过255")
    private String reason;
    @ApiModelProperty(value = "")
    private Long userId;
    /**
     * 租户code
     */
    @ApiModelProperty(value = "租户code")
    @Length(max = 32, message = "租户code长度不能超过32")
    private String tenantId;
    /**
     * 流程实例外键
     */
    @ApiModelProperty(value = "流程实例外键")
    @Length(max = 64, message = "流程实例外键长度不能超过64")
    private String instId;
    /**
     * 删除标识
     */
    @ApiModelProperty(value = "删除标识")
    private Boolean isDelete;
    /**
     * 流程是否结束
     */
    @ApiModelProperty(value = "流程是否结束")
    private Boolean isOver;

    /**
     * 是否只看本人的
     */
    @ApiModelProperty(value = "流程是否结束")
    private Boolean isMine;
}
