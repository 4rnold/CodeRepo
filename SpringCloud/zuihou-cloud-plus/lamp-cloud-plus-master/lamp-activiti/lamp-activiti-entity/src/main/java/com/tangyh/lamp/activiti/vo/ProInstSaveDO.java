//package com.tangyh.lamp.activiti.vo;
//
//import cn.afterturn.easypoi.excel.annotation.Excel;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.tangyh.basic.base.entity.Entity;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import org.hibernate.validator.constraints.Length;
//
//import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;
//
//
///**
// * 流程实例新增实体
// *
// * @author wz
// * @date 2020-08-07
// */
//@Data
//public class ProInstSaveDO<T> extends Entity<Long>{
//    /**
//     * 流程实例外键
//     */
//    @ApiModelProperty(value = "流程实例外键")
//    @Length(max = 64, message = "流程实例外键不能超过64")
//    @TableField(value = "INST_ID", condition = LIKE)
//    @Excel(name = "流程实例外键")
//    protected String instId;
//}
