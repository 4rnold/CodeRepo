package com.tangyh.lamp.activiti.service.activiti;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.ImmutableMap;
import com.tangyh.basic.base.R;
import com.tangyh.basic.base.entity.SuperEntity;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.basic.utils.CollHelper;
import com.tangyh.lamp.activiti.dto.activiti.InstantSelectReqDTO;
import com.tangyh.lamp.activiti.dto.activiti.ProcessInstanceResDTO;
import com.tangyh.lamp.activiti.exception.MyActivitiExceptionCode;
import com.tangyh.lamp.activiti.exception.MyException;
import com.tangyh.lamp.authority.api.UserBizApi;
import com.tangyh.lamp.authority.entity.auth.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程示例业务
 *
 * @author wz
 * @date 2020-08-07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyProcessInstantService {

    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final IdentityService identityService;
    private final UserBizApi userBizApi;

    /**
     * 保存实体
     *
     * @param entity    保存实体
     * @param key       流程定义key
     * @param variables 流程变量
     */
    @Transactional(rollbackFor = Exception.class)
    public <T extends SuperEntity> ProcessInstance add(T entity, String key, Map<String, Object> variables) {
        identityService.setAuthenticatedUserId(String.valueOf(entity.getCreatedBy()));
        String bussKey = String.valueOf(entity.getId());
        try {
            return runtimeService.startProcessInstanceByKeyAndTenantId(key, bussKey, variables, ContextUtil.getTenant());
        } catch (ActivitiException ex) {
            MyException.exception(MyActivitiExceptionCode.PROCESSINST_UPDATE_ERR);
            return null;
        }
    }

    /**
     * 分页查询实例信息
     */
    @Transactional(rollbackFor = Exception.class)
    public IPage<ProcessInstanceResDTO> page(PageParams<InstantSelectReqDTO> pageParams) {
        IPage<ProcessInstanceResDTO> page = pageParams.buildPage().setRecords(new ArrayList());
        InstantSelectReqDTO params = pageParams.getModel();
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        if (StrUtil.isNotEmpty(params.getName())) {
            processInstanceQuery.processInstanceName(params.getName());
        }
        if (StrUtil.isNotEmpty(params.getKey())) {
            processInstanceQuery.processInstanceBusinessKey(params.getKey());
        }
        processInstanceQuery.processInstanceTenantId(ContextUtil.getTenant());

        List<ProcessInstance> processInstances = processInstanceQuery.listPage((int) page.offset(), (int) page.getSize());

        List<ProcessInstanceResDTO> res = new ArrayList<>();
        processInstances.forEach(obj -> res.add(ProcessInstanceResDTO.builder()
                .id(obj.getId())
                .tenantId(obj.getTenantId())
                .startTime(obj.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .businessKey(obj.getBusinessKey())
                .name(obj.getName())
                .deploymentId(obj.getDeploymentId())
                .processDefinitionId(obj.getProcessDefinitionId())
                .processDefinitionKey(obj.getProcessDefinitionKey())
                .processDefinitionName(obj.getProcessDefinitionName())
                .rootProcessInstanceId(obj.getRootProcessInstanceId())
                .procInstId(obj.getProcessInstanceId())
                .suspendState(obj.isSuspended() ? "2" : "1")
                .value(obj.getProcessVariables())
                .build()));

        // 查询相关变量
        if (CollUtil.isNotEmpty(res)) {
            Set<String> ids = res.stream().map(ProcessInstanceResDTO::getId).collect(Collectors.toSet());
            List<HistoricVariableInstance> executionsVar = historyService.createHistoricVariableInstanceQuery().executionIds(ids).list();
            res.forEach(obj -> executionsVar.forEach(exe -> {
                if (exe.getProcessInstanceId().equals(obj.getId())) {
                    obj.getValue().put(exe.getVariableName(), exe.getValue());
                }
            }));
        }

        page.setTotal(processInstanceQuery.count());
        page.setRecords(res);
        return page;
    }

    /**
     * 获取实例详情
     *
     * @param instantId 实例id
     */
    @Transactional(rollbackFor = Exception.class)
    public ProcessInstanceResDTO getDetail(String instantId) {
        ProcessInstance obj = runtimeService.createProcessInstanceQuery().processInstanceId(instantId).singleResult();
        if (obj == null) {
            MyException.exception(MyActivitiExceptionCode.DATA_NOT_FOUNT);
        }

        ProcessInstanceResDTO res = ProcessInstanceResDTO.builder()
                .id(obj.getId())
                .tenantId(obj.getTenantId())
                .startUser(new RemoteData<>(Long.valueOf(obj.getStartUserId())))
                .startTime(obj.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .businessKey(obj.getBusinessKey())
                .name(obj.getName())
                .deploymentId(obj.getDeploymentId())
                .processDefinitionId(obj.getProcessDefinitionId())
                .processDefinitionKey(obj.getProcessDefinitionKey())
                .processDefinitionName(obj.getProcessDefinitionName())
                .rootProcessInstanceId(obj.getRootProcessInstanceId())
                .procInstId(obj.getProcessInstanceId())
                .value(new HashMap<>(16))
                .build();

        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(instantId).list();
        list.forEach(exe -> {
            if (exe.getProcessInstanceId().equals(res.getId())) {
                res.getValue().put(exe.getVariableName(), exe.getValue());
            }
        });
        return res;
    }

    /**
     * 修改流程实例状态
     *
     * @param id           实例id
     * @param suspendState 状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void suspendOrActiveApply(String id, String suspendState) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(id)
                .processInstanceTenantId(ContextUtil.getTenant()).singleResult();
        if (instance == null) {
            MyException.exception(MyActivitiExceptionCode.LOGIN_HAVE_NOT_AUTH);
        }

        if ("1".equals(suspendState)) {
            runtimeService.suspendProcessInstanceById(id);
        } else if ("2".equals(suspendState)) {
            runtimeService.activateProcessInstanceById(id);
        }
    }

    /**
     * 删除流程实例
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteProcessInstantByIds(List<String> ids) {
        int counter = 0;
        for (String instId : ids) {
            runtimeService.deleteProcessInstance(instId, "");
            counter++;
        }
        return counter;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean isOver(String instId) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instId).singleResult();

        if (pi == null) {
            log.info("流程已经结束");
            return true;
        } else {
            log.info("流程没有结束");
            return false;
        }
    }

    /**
     * 转换
     *
     * @param ids 流程实例id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<Serializable, ProcessInstanceResDTO> findProInst(Set<Serializable> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<String> set = new HashSet();
        ids.forEach(id -> set.add(String.valueOf(id)));

        // 1. 根据 字典编码查询可用的字典列表
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceIds(set).list();
        List<ProcessInstanceResDTO> list = new ArrayList<>();

        processInstances.forEach(obj -> list.add(ProcessInstanceResDTO.builder()
                .id(obj.getId())
                .tenantId(obj.getTenantId())
                .startTime(obj.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .startUser(new RemoteData<>(Long.valueOf(obj.getStartUserId())))
                .businessKey(obj.getBusinessKey())
                .name(obj.getName())
                .deploymentId(obj.getDeploymentId())
                .processDefinitionId(obj.getProcessDefinitionId())
                .processDefinitionKey(obj.getProcessDefinitionKey())
                .processDefinitionName(obj.getProcessDefinitionName())
                .rootProcessInstanceId(obj.getRootProcessInstanceId())
                .procInstId(obj.getProcessInstanceId())
                .suspendState(obj.isSuspended() ? "2" : "1")
                .value(obj.getProcessVariables())
                .build()));

        List<Long> userIds = list.stream().map(inst -> inst.getStartUser().getKey()).collect(Collectors.toList());

        R<List<User>> users = userBizApi.findUserById(userIds);
        if (CollUtil.isNotEmpty(users.getData())) {
            List<User> data = users.getData();
            list.forEach(inst -> data.forEach(user -> {
                if (user.getId().equals(inst.getStartUser().getKey())) {
                    inst.getStartUser().setData(user.getName());
                }
            }));
        }

        // 2. 将 list 转换成 Map，Map的key是字典编码，value是字典名称
        ImmutableMap<String, ProcessInstanceResDTO> typeMap = CollHelper.uniqueIndex(list,
                ProcessInstanceResDTO::getId
                , (item) -> item);

        // 3. 将 Map<String, String> 转换成 Map<Serializable, Object>
        Map<Serializable, ProcessInstanceResDTO> typeCodeNameMap = new HashMap<>(CollHelper.initialCapacity(typeMap.size()));
        typeMap.forEach(typeCodeNameMap::put);
        return typeCodeNameMap;
    }

    /**
     * 转换
     *
     * @param ids 流程实例id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<Serializable, ProcessInstanceResDTO> findProHiInst(Set<Serializable> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        HashSet set = new HashSet();
        ids.forEach(id -> set.add(String.valueOf(id)));

        // 1. 根据 字典编码查询可用的字典列表
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().processInstanceIds(set).list();
        List<ProcessInstanceResDTO> list = new ArrayList<>();

        processInstances.forEach(obj -> list.add(ProcessInstanceResDTO.builder()
                .id(obj.getId())
                .tenantId(obj.getTenantId())
                .startTime(obj.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .startUser(new RemoteData<>(Long.valueOf(obj.getStartUserId())))
                .businessKey(obj.getBusinessKey())
                .name(obj.getName())
                .deploymentId(obj.getDeploymentId())
                .processDefinitionId(obj.getProcessDefinitionId())
                .processDefinitionKey(obj.getProcessDefinitionKey())
                .processDefinitionName(obj.getProcessDefinitionName())
                .procInstId(obj.getId())
                .value(obj.getProcessVariables())
                .build()));

        List<Long> userIds = list.stream().map(inst -> inst.getStartUser().getKey()).collect(Collectors.toList());

        R<List<User>> users = userBizApi.findUserById(userIds);
        if (CollUtil.isNotEmpty(users.getData())) {
            List<User> data = users.getData();
            list.forEach(inst -> data.forEach(user -> {
                if (user.getId().equals(inst.getStartUser().getKey())) {
                    inst.getStartUser().setData(user.getName());
                }
            }));
        }

        // 2. 将 list 转换成 Map，Map的key是字典编码，value是字典名称
        ImmutableMap<String, ProcessInstanceResDTO> typeMap = CollHelper.uniqueIndex(list,
                ProcessInstanceResDTO::getId
                , (item) -> item);

        // 3. 将 Map<String, String> 转换成 Map<Serializable, Object>
        Map<Serializable, ProcessInstanceResDTO> typeCodeNameMap = new HashMap<>(CollHelper.initialCapacity(typeMap.size()));
        typeMap.forEach(typeCodeNameMap::put);
        return typeCodeNameMap;
    }
}
