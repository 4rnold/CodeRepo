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
import com.tangyh.lamp.activiti.constant.LeaveVarConstant;
import com.tangyh.lamp.activiti.dao.biz.BizLeaveMapper;
import com.tangyh.lamp.activiti.domain.core.UpdateCollEntity;
import com.tangyh.lamp.activiti.dto.activiti.TaskResDTO;
import com.tangyh.lamp.activiti.dto.biz.BizLeavePageDTO;
import com.tangyh.lamp.activiti.dto.biz.BizLeaveResDTO;
import com.tangyh.lamp.activiti.entity.biz.BizItem;
import com.tangyh.lamp.activiti.entity.biz.BizLeave;
import com.tangyh.lamp.activiti.exception.MyActivitiExceptionCode;
import com.tangyh.lamp.activiti.exception.MyException;
import com.tangyh.lamp.activiti.service.activiti.MyProcessInstantService;
import com.tangyh.lamp.activiti.service.activiti.MyTaskService;
import com.tangyh.lamp.activiti.service.biz.BizItemService;
import com.tangyh.lamp.activiti.service.biz.BizLeaveService;
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
 * 请假流程
 * </p>
 *
 * @author wz
 * @date 2020-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BizLeaveServiceImpl extends SuperServiceImpl<BizLeaveMapper, BizLeave> implements BizLeaveService {
    /**
     * 请假流程key
     */
    private static final String BIZ_KEY = "my_leave";

    private final MyProcessInstantService myProcessInstantService;
    private final MyTaskService myTaskService;
    private final BizItemService bizItemService;

    @Override
    public String getKey() {
        return BIZ_KEY;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBiz(BizLeave bizLeave) {
        bizLeave.setName(ContextUtil.getName());
        bizLeave.setTenantCode(ContextUtil.getTenant());
        save(bizLeave);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(LeaveVarConstant.LEAVE_USER, bizLeave.getName());
        map.put(LeaveVarConstant.START_TIME, bizLeave.getStartTime());
        map.put(LeaveVarConstant.END_TIME, bizLeave.getEndTime());
        map.put(LeaveVarConstant.LEAVE_LONG, bizLeave.getWhenLong());
        map.put(LeaveVarConstant.LEAVE_TYPE, bizLeave.getType());
        map.put(LeaveVarConstant.LEAVE_REASON, bizLeave.getReason());
        map.put(LeaveVarConstant.USERNAME, bizLeave.getCreatedBy());

        ProcessInstance pi = myProcessInstantService.add(bizLeave, getKey(), map);
        bizLeave.setInstId(pi.getId());
        bizLeave.setInst(new RemoteData<>(bizLeave.getInstId()));
        updateById(bizLeave);

        // 该流程为驳回流程示例，驳回不会结束流程，需要重新提交，那么第一次发起填单自动提交
        List<TaskResDTO> readyTaskByInst = myTaskService.getReadyTaskByInst(pi.getId());
        if (!CollUtil.isEmpty(readyTaskByInst)) {
            TaskResDTO resDTO = readyTaskByInst.get(0);
            BizItem item = BizItem.builder()
                    .bizId(bizLeave.getId())
                    .instId(pi.getId())
                    .taskId(resDTO.getId())
                    .itemName(resDTO.getName())
                    .itemRemake("发起提交")
                    .result(true)
                    .tenantCode(bizLeave.getTenantCode())
                    .build();
            bizItemService.saveItem(item);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteBiz(UpdateCollEntity<String> entity) {
        List<String> ids = entity.getIds();
        List<BizLeave> bizLeaves = listByIds(ids);

        List<String> instIds = bizLeaves.stream().map(item -> RemoteData.getKey(item.getInst())).collect(Collectors.toList());
        int count = myProcessInstantService.deleteProcessInstantByIds(instIds);

        if (count == 0) {
            MyException.exception(MyActivitiExceptionCode.DATA_NOT_FOUNT);
        }

        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IPage<BizLeaveResDTO> pageBiz(PageParams<BizLeavePageDTO> params) {
        IPage<BizLeave> page = params.buildPage();
        BizLeave model = BeanPlusUtil.toBean(params.getModel(), BizLeave.class);
        QueryWrap<BizLeave> wrapper = Wraps.q(model);
        page(page, wrapper);
        IPage<BizLeaveResDTO> res = BeanPlusUtil.toBeanPage(page, BizLeaveResDTO.class);
        res.getRecords().forEach(obj -> obj.setInst(obj.getInst()));
        return res;
    }

    /**
     * 转换
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<Serializable, BizLeave> findBizByInstId(Set<Serializable> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        // 1. 根据 字典编码查询可用的字典列表
        LbuWrapper<BizLeave> wrapper = Wraps.<BizLeave>lbU().in(BizLeave::getInst, ids);
        List<BizLeave> list = list(wrapper);

        // 2. 将 list 转换成 Map，Map的key是字典编码，value是字典名称
        ImmutableMap<String, BizLeave> typeMap = CollHelper.uniqueIndex(list, item -> item.getInst().getKey(), item -> item);

        // 3. 将 Map<String, String> 转换成 Map<Serializable, Object>
        Map<Serializable, BizLeave> typeCodeNameMap = new HashMap<>(CollHelper.initialCapacity(typeMap.size()));
        typeMap.forEach(typeCodeNameMap::put);
        return typeCodeNameMap;
    }
}
