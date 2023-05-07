package com.tangyh.lamp.activiti.dto.biz;

import com.tangyh.basic.annotation.injection.InjectionField;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.lamp.activiti.dto.activiti.TaskResDTO;
import com.tangyh.lamp.activiti.entity.biz.BizLeave;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class TaskLeaveResDTO extends TaskResDTO {
    /**
     * 对应业务实例
     */
    @InjectionField(api = "bizLeaveServiceImpl", method = "findBizByInstId")
    private RemoteData<String, BizLeave> biz;

}
