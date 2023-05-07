package com.tangyh.lamp.activiti.entity.biz;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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

import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * <p>
 * 实体类
 *
 * </p>
 *
 * @author wz
 * @since 2020-08-19
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_biz_item")
@ApiModel(value = "BizItem", description = "业务节点")
@AllArgsConstructor
public class BizItem extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 事项名称
     */
    @ApiModelProperty(value = "事项名称")
    @Length(max = 64, message = "事项名称长度不能超过64")
    @TableField(value = "item_name", condition = LIKE)
    @Excel(name = "事项名称")
    private String itemName;

    /**
     * 事项内容
     */
    @ApiModelProperty(value = "事项内容")
    @Length(max = 64, message = "事项内容长度不能超过64")
    @TableField(value = "item_content", condition = LIKE)
    @Excel(name = "事项内容")
    private String itemContent;

    /**
     * 事项备注
     */
    @ApiModelProperty(value = "事项备注")
    @Length(max = 255, message = "事项备注长度不能超过255")
    @TableField(value = "item_remake", condition = LIKE)
    @Excel(name = "事项备注")
    private String itemRemake;

    /**
     * 租户code
     */
    @ApiModelProperty(value = "租户code")
    @Length(max = 255, message = "租户code")
    @TableField(value = "tenant_code", condition = LIKE)
    @Excel(name = "租户code")
    private String tenantCode;

    /**
     * 任务外键
     */
    @ApiModelProperty(value = "任务外键")
    @Length(max = 64, message = "任务外键长度不能超过64")
    @TableField(value = "task_id", condition = LIKE)
    @Excel(name = "任务外键")
    private String taskId;

    /**
     * 流程实例外键
     */
    @ApiModelProperty(value = "流程实例外键")
    @Length(max = 64, message = "流程实例外键长度不能超过64")
    @TableField(value = "inst_id", condition = LIKE)
    @Excel(name = "流程实例外键")
    private String instId;

    /**
     * 模块名
     */
    @ApiModelProperty(value = "模块名")
    @Length(max = 255, message = "模块名长度不能超过255")
    @TableField(value = "module_", condition = LIKE)
    @Excel(name = "模块名")
    private String module;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    @TableField("biz_id")
    @Excel(name = "业务id")
    private Long bizId;

    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    @TableField("result")
    @Excel(name = "审批结果", replace = {"是_true", "否_false", "_null"})
    private Boolean result;

    /**
     * 事项待办人
     */
    @ApiModelProperty(value = "事项待办人")
    @TableField("item_user")
    @Excel(name = "事项待办人")
    private Long itemUser;


    @Builder
    public BizItem(Long id, LocalDateTime createTime, Long createUser, LocalDateTime updateTime, Long updateUser,
                   String itemName, String itemContent, String itemRemake, String tenantCode, String taskId,
                   String instId, String module, Long bizId, Boolean result, Long itemUser) {
        this.id = id;
        this.createTime = createTime;
        this.createdBy = createUser;
        this.updateTime = updateTime;
        this.updatedBy = updateUser;
        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemRemake = itemRemake;
        this.tenantCode = tenantCode;
        this.taskId = taskId;
        this.instId = instId;
        this.module = module;
        this.bizId = bizId;
        this.result = result;
        this.itemUser = itemUser;
    }

}
