package com.tangyh.lamp.activiti.dto.biz;

import com.tangyh.lamp.activiti.entity.biz.BizLeave;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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
@ApiModel(value = "BizLeaveRes", description = "请假流程返回复合实体")
public class BizLeaveResDTO extends BizLeave {

//    /**
//     * 所属流程实例
//     */
//    @InjectionField(api = "myProcessInstantService", method = "findProInst")
//    protected RemoteData<String, ProcessInstanceResDTO> inst;
}
