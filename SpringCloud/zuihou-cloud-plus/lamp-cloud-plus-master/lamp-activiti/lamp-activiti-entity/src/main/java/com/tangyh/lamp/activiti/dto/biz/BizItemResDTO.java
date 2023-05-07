package com.tangyh.lamp.activiti.dto.biz;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangyh.lamp.activiti.entity.biz.BizItem;
import com.tangyh.lamp.authority.entity.auth.User;
import com.tangyh.basic.base.entity.Entity;
import com.tangyh.lamp.common.constant.InjectionFieldConstants;
import com.tangyh.basic.annotation.injection.InjectionField;
import com.tangyh.basic.model.RemoteData;
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
public class BizItemResDTO extends BizItem{

    /**
     * 实体项公共信息-用户
     */
    private User cUser;
}
