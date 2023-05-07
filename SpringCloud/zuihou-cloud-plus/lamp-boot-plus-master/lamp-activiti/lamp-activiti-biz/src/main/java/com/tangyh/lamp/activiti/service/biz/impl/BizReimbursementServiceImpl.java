package com.tangyh.lamp.activiti.service.biz.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.ImmutableMap;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.base.service.SuperServiceImpl;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.database.mybatis.conditions.Wraps;
import com.tangyh.basic.database.mybatis.conditions.query.QueryWrap;
import com.tangyh.basic.database.mybatis.conditions.update.LbuWrapper;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.basic.utils.BeanPlusUtil;
import com.tangyh.basic.utils.CollHelper;
import com.tangyh.lamp.activiti.constant.ReimbursementVarConstant;
import com.tangyh.lamp.activiti.dao.biz.BizReimbursementMapper;
import com.tangyh.lamp.activiti.domain.core.UpdateCollEntity;
import com.tangyh.lamp.activiti.dto.activiti.TaskResDTO;
import com.tangyh.lamp.activiti.dto.biz.BizReimbursementPageDTO;
import com.tangyh.lamp.activiti.dto.biz.BizReimbursementResDTO;
import com.tangyh.lamp.activiti.entity.biz.BizItem;
import com.tangyh.lamp.activiti.entity.biz.BizReimbursement;
import com.tangyh.lamp.activiti.exception.MyActivitiExceptionCode;
import com.tangyh.lamp.activiti.exception.MyException;
import com.tangyh.lamp.activiti.service.activiti.MyProcessInstantService;
import com.tangyh.lamp.activiti.service.activiti.MyTaskService;
import com.tangyh.lamp.activiti.service.biz.BizItemService;
import com.tangyh.lamp.activiti.service.biz.BizReimbursementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 报销流程
 * </p>
 *
 * @author wz
 * @date 2020-08-31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BizReimbursementServiceImpl extends SuperServiceImpl<BizReimbursementMapper, BizReimbursement> implements BizReimbursementService {
    private static final String BIZ_KEY = "my_reimbursement";

    @Override
    public String getKey() {
        return BIZ_KEY;
    }

    private final MyProcessInstantService myProcessInstantService;
    private final MyTaskService myTaskService;
    private final BizItemService bizItemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBiz(BizReimbursement bizReimbursement) {
        bizReimbursement.setName(ContextUtil.getName());
        bizReimbursement.setTenantCode(ContextUtil.getTenant());
        save(bizReimbursement);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(ReimbursementVarConstant.REIMBURSEMENT_USER, bizReimbursement.getName());
        map.put(ReimbursementVarConstant.REIMBURSEMENT_TYPE, bizReimbursement.getType());
        map.put(ReimbursementVarConstant.REIMBURSEMENT_REASON, bizReimbursement.getReason());
        map.put(ReimbursementVarConstant.REIMBURSEMENT_NUMBER, bizReimbursement.getAmount());
        map.put(ReimbursementVarConstant.USERNAME, bizReimbursement.getCreatedBy());

        ProcessInstance pi = myProcessInstantService.add(bizReimbursement, BIZ_KEY, map);
        bizReimbursement.setInstId(pi.getId());
        bizReimbursement.setInst(new RemoteData<>(bizReimbursement.getInstId()));
        updateById(bizReimbursement);

        // 该流程为驳回流程示例，驳回不会结束流程，需要重新提交，那么第一次发起填单自动提交
        List<TaskResDTO> readyTaskByInst = myTaskService.getReadyTaskByInst(pi.getId());
        if (!CollUtil.isEmpty(readyTaskByInst)) {
            TaskResDTO resDTO = readyTaskByInst.get(0);
            BizItem item = BizItem.builder()
                    .bizId(bizReimbursement.getId())
                    .instId(pi.getId())
                    .taskId(resDTO.getId())
                    .itemName(resDTO.getName())
                    .itemRemake("发起提交")
                    .result(true)
                    .tenantCode(bizReimbursement.getTenantCode())
                    .build();
            bizItemService.saveItem(item);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBiz(UpdateCollEntity<String> entity) {
        List<String> ids = entity.getIds();
        List<BizReimbursement> bizReimbursements = listByIds(ids);

        List<String> instIds = bizReimbursements.stream().map(item -> RemoteData.getKey(item.getInst())).collect(Collectors.toList());
        int count = myProcessInstantService.deleteProcessInstantByIds(instIds);

        if (count == 0) {
            MyException.exception(MyActivitiExceptionCode.DATA_NOT_FOUNT);
        }

        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IPage<BizReimbursementResDTO> pageBiz(PageParams<BizReimbursementPageDTO> params) {
        IPage<BizReimbursement> page = params.buildPage();
        BizReimbursement model = BeanPlusUtil.toBean(params.getModel(), BizReimbursement.class);
        QueryWrap<BizReimbursement> wrapper = Wraps.q(model);
        page(page, wrapper);
        IPage<BizReimbursementResDTO> res = BeanPlusUtil.toBeanPage(page, BizReimbursementResDTO.class);
        res.getRecords().forEach(obj -> obj.setInst(obj.getInst()));
        return res;
    }

    /**
     * 转换
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<Serializable, BizReimbursement> findBizByInstId(Set<Serializable> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        // 1. 根据 字典编码查询可用的字典列表
        LbuWrapper<BizReimbursement> wrapper = Wraps.<BizReimbursement>lbU().in(BizReimbursement::getInst, ids);
        List<BizReimbursement> list = list(wrapper);

        // 2. 将 list 转换成 Map，Map的key是字典编码，value是字典名称
        ImmutableMap<String, BizReimbursement> typeMap = CollHelper.uniqueIndex(list, item -> RemoteData.getKey(item.getInst()), item -> item);

        // 3. 将 Map<String, String> 转换成 Map<Serializable, Object>
        Map<Serializable, BizReimbursement> typeCodeNameMap = new HashMap<>(CollHelper.initialCapacity(typeMap.size()));
        typeMap.forEach(typeCodeNameMap::put);
        return typeCodeNameMap;
    }
}
