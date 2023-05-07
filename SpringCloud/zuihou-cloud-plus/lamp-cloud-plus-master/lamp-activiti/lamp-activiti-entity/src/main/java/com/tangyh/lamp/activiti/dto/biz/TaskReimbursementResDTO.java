package com.tangyh.lamp.activiti.dto.biz;

import com.tangyh.basic.annotation.injection.InjectionField;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.lamp.activiti.dto.activiti.TaskResDTO;
import com.tangyh.lamp.activiti.entity.biz.BizReimbursement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 活动任务返回实体
 *
 * @author wz
 * @date 2020-08-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TaskReimbursementResDTO extends TaskResDTO {

    /**
     * 对应业务实例
     */
    @InjectionField(api = "bizReimbursementServiceImpl", method = "findBizByInstId")
    private RemoteData<String, BizReimbursement> biz;

}
