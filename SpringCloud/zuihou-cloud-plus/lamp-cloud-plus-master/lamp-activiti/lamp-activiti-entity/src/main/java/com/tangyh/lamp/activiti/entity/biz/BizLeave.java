package com.tangyh.lamp.activiti.entity.biz;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangyh.basic.annotation.injection.InjectionField;
import com.tangyh.basic.base.entity.Entity;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.lamp.activiti.dto.activiti.ProcessInstanceResDTO;
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

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;
import static com.tangyh.basic.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

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
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_biz_leave")
@ApiModel(value = "BizLeave", description = "请假流程")
@AllArgsConstructor
public class BizLeave extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 流程实例外键
     */
    @ApiModelProperty(value = "流程实例外键")
    @Length(max = 64, message = "流程实例外键不能超过64")
    @TableField(value = "inst_id", condition = EQUAL)
    @InjectionField(api = "myProcessInstantService", method = "findProInst")
    protected RemoteData<String, ProcessInstanceResDTO> inst;
    /**
     * 请假人员
     */
    @ApiModelProperty(value = "请假人员")
    @Length(max = 32, message = "请假人员长度不能超过32")
    @TableField(value = "name_", condition = LIKE)
    @Excel(name = "请假人员")
    private String name;
    /**
     * 请假时间
     */
    @ApiModelProperty(value = "请假时间")
    @TableField("start_time")
    @Excel(name = "请假时间", format = DEFAULT_DATE_TIME_FORMAT, width = 20)
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    @Excel(name = "结束时间", format = DEFAULT_DATE_TIME_FORMAT, width = 20)
    private LocalDateTime endTime;
    /**
     * 请假时长
     */
    @ApiModelProperty(value = "请假时长")
    @Length(max = 255, message = "请假时长长度不能超过255")
    @TableField(value = "when_long", condition = LIKE)
    @Excel(name = "请假时长")
    private String whenLong;
    /**
     * 请假类型
     */
    @ApiModelProperty(value = "请假类型")
    @Length(max = 8, message = "请假类型长度不能超过8")
    @TableField(value = "type", condition = LIKE)
    @Excel(name = "请假类型")
    private String type;
    /**
     * 请假人
     */
    @ApiModelProperty(value = "请假人")
    @TableField(value = "user_id")
    @Excel(name = "请假人")
    private Long userId;
    /**
     * 请假事由
     */
    @ApiModelProperty(value = "请假事由")
    @Length(max = 255, message = "请假事由长度不能超过255")
    @TableField(value = "reason", condition = LIKE)
    @Excel(name = "请假事由")
    private String reason;

    /**
     * 租户code
     */
    @ApiModelProperty(value = "租户code")
    @Length(max = 255, message = "租户code")
    @TableField(value = "tenant_code", condition = LIKE)
    @Excel(name = "租户code")
    private String tenantCode;

    /**
     * 流程实例外键
     */
    @ApiModelProperty(value = "流程实例外键")
    @Length(max = 64, message = "流程实例外键长度不能超过64")
    @TableField(value = "inst_id", condition = LIKE)
    @Excel(name = "流程实例外键")
    private String instId;

    /**
     * 流程是否结束
     */
    @ApiModelProperty(value = "流程是否结束")
    @TableField("is_over")
    @Excel(name = "流程是否结束", replace = {"是_true", "否_false", "_null"})
    private Boolean isOver;

    @Builder
    public BizLeave(Long id, LocalDateTime createTime, Long createUser, LocalDateTime updateTime, Long updateUser,
                    String name, LocalDateTime startTime, LocalDateTime endTime, String whenLong, String type, Long userId,
                    String reason, String tenantCode, String instId, RemoteData<String, ProcessInstanceResDTO> inst, Boolean isOver) {
        this.id = id;
        this.createTime = createTime;
        this.createdBy = createUser;
        this.updateTime = updateTime;
        this.updatedBy = updateUser;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.whenLong = whenLong;
        this.type = type;
        this.reason = reason;
        this.tenantCode = tenantCode;
        this.instId = instId;
        this.isOver = isOver;
        this.inst = inst;
        this.userId = userId;
    }

}
