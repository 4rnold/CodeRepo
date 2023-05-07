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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.EQUAL;
import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * <p>
 * 实体类
 * 报销流程
 * </p>
 *
 * @author wz
 * @since 2020-08-31
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_biz_reimbursement")
@ApiModel(value = "BizReimbursement", description = "报销流程")
@AllArgsConstructor
public class BizReimbursement extends Entity<Long> {

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
     * 报销人员
     */
    @ApiModelProperty(value = "报销人员")
    @Length(max = 32, message = "报销人员长度不能超过32")
    @TableField(value = "name_", condition = LIKE)
    @Excel(name = "报销人员")
    private String name;
    /**
     * 报销类型
     */
    @ApiModelProperty(value = "报销类型")
    @Length(max = 8, message = "报销类型长度不能超过8")
    @TableField(value = "type", condition = LIKE)
    @Excel(name = "报销类型")
    private String type;
    /**
     * 报销说明
     */
    @ApiModelProperty(value = "报销说明")
    @Length(max = 255, message = "报销说明长度不能超过255")
    @TableField(value = "reason", condition = LIKE)
    @Excel(name = "报销说明")
    private String reason;

    @ApiModelProperty(value = "报销金额")
    @TableField("amount")
    @Excel(name = "报销金额")
    private BigDecimal amount;

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
    /**
     * 报销人
     */
    @ApiModelProperty(value = "报销人")
    @TableField(value = "user_id")
    @Excel(name = "报销人")
    private Long userId;

    @Builder
    public BizReimbursement(Long id, LocalDateTime createTime, Long createUser, LocalDateTime updateTime, Long updateUser,
                            Long userId, RemoteData<String, ProcessInstanceResDTO> inst,
                            String name, String type, String reason, BigDecimal amount, String tenantCode,
                            String instId, Boolean isOver) {
        super(id, createTime, createUser, updateTime, updateUser);
        this.inst = inst;
        this.name = name;
        this.type = type;
        this.reason = reason;
        this.amount = amount;
        this.tenantCode = tenantCode;
        this.instId = instId;
        this.isOver = isOver;
        this.userId = userId;
    }
}
