package com.tangyh.lamp.activiti.dto.biz;

import com.tangyh.lamp.activiti.entity.biz.BizReimbursement;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 实体类
 * 报销流程
 * </p>
 *
 * @author wz
 * @since 2020-08-20
 */
@Data
@ApiModel(value = "BizReimbursementRes", description = "报销流程返回复合实体")
public class BizReimbursementResDTO extends BizReimbursement {

//    /**
//     * 所属流程实例
//     */
//    @InjectionField(api = "myProcessInstantService", method = "findProInst")
//    protected RemoteData<String, ProcessInstanceResDTO> inst;
}
