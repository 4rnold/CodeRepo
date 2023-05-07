package com.tangyh.lamp.activiti.service.activiti;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.basic.utils.StrHelper;
import com.tangyh.lamp.activiti.dto.activiti.InstantSelectReqDTO;
import com.tangyh.lamp.activiti.dto.activiti.TaskHiResDTO;
import com.tangyh.lamp.activiti.dto.activiti.TaskResDTO;
import com.tangyh.lamp.activiti.exception.MyActivitiExceptionCode;
import com.tangyh.lamp.activiti.exception.MyException;
import com.tangyh.lamp.authority.api.UserBizApi;
import com.tangyh.lamp.authority.entity.auth.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程示例业务
 *
 * @author wz
 * @date 2020-08-07
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MyTaskService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final UserBizApi userBizApi;

    /**
     * 查找需要处理的任务
     *
     * @param pageParams 分页参数
     */
    @Transactional(readOnly = true)
    public IPage<TaskHiResDTO> pageDealtWithHiTasks(PageParams<InstantSelectReqDTO> pageParams) {
        IPage<TaskHiResDTO> page = pageParams.buildPage().setRecords(new ArrayList());
        InstantSelectReqDTO data = pageParams.getModel();
        String userId = data.getUserId();

        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(ContextUtil.getTenant())
                .processDefinitionKey(data.getKey())
                .taskAssignee(userId)
                .finished()
                .orderByTaskCreateTime().desc();
        if (StrUtil.isNotEmpty(data.getName())) {
            query.taskNameLike(StrHelper.fullLike(data.getName()));
        }

        page.setTotal(query.count());
        if (page.getTotal() > 0) {
            List<HistoricTaskInstance> todoList = query.listPage((int) page.offset(), (int) page.getSize());
            List<TaskHiResDTO> tasks = new ArrayList<>();
            todoList.forEach(task -> tasks.add(getHiTaskRes(task)));
            page.setRecords(tasks);
        }
        return page;
    }


    /**
     * 查找已办理的任务
     *
     * @param pageParams 分页实体
     */
    @Transactional(readOnly = true)
    public <T extends TaskResDTO> IPage<T> pageDealtWithRunTasks(PageParams<InstantSelectReqDTO> pageParams) {
        IPage<T> page = pageParams.buildPage().setRecords(new ArrayList());
        InstantSelectReqDTO data = pageParams.getModel();

        List<T> tasks = new ArrayList<>();
        TaskQuery query = taskService.createTaskQuery().processDefinitionKey(data.getKey())
                .taskCandidateOrAssigned(data.getUserId())
                .orderByTaskCreateTime().desc();

        if (StrUtil.isNotEmpty(data.getName())) {
            query.taskNameLike(StrHelper.fullLike(data.getName()));
        }
        long total = query.count();
        page.setTotal(total);
        if (total > 0) {
            List<Task> todoList = query.listPage((int) page.offset(), (int) page.getSize());
            todoList.forEach(task -> tasks.add(getTaskRes(task)));
            page.setRecords(tasks);
        }
        return page;
    }

    /**
     * 设置绑定任务流程变量
     *
     * @param taskId 任务
     * @param map    流程变量
     */
    @Transactional(rollbackFor = Exception.class)
    public void setVariablesLocal(String taskId, Map<String, Object> map) {
        try {
            taskService.setVariablesLocal(taskId, map);
        } catch (ActivitiException ex) {
            MyException.exception(MyActivitiExceptionCode.TASK_UPDATE_ERR);
        }
    }

    /**
     * 设置流程变量
     *
     * @param taskId 任务id
     * @param key    流程变量KEY
     * @param value  流程变量值
     */
    @Transactional(rollbackFor = Exception.class)
    public void setVariables(String taskId, String key, String value) {
        taskService.setVariable(taskId, key, value);
    }

    /**
     * 完成任务
     *
     * @param taskId 任务id
     */
    @Transactional(rollbackFor = Exception.class)
    public void complete(String taskId) {
        taskService.complete(taskId);
    }

    /**
     * 指派事项代理人
     *
     * @param taskId   任务id
     * @param assignee 用户id
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean setAssignee(String taskId, String assignee) {
        taskService.setAssignee(taskId, assignee);
        return true;
    }

    /**
     * 删除流程变量
     *
     * @param instId 流程id
     * @param key    流程变量key
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeVariable(String instId, String key) {
        runtimeService.removeVariable(instId, key);
    }

    /**
     * 获取流程当前正执行任务
     *
     * @param instId 流程id
     */
    @Transactional(rollbackFor = Exception.class)
    public <T extends TaskResDTO> List<T> getReadyTaskByInst(String instId) {
        List<Task> task = taskService.createTaskQuery().processInstanceId(instId).list();
        List<T> res = new ArrayList<>();
        task.forEach(obj -> res.add(getTaskRes(obj)));
        if (CollUtil.isNotEmpty(res)) {
            List<Long> ids = res.stream().map(o -> Long.valueOf(o.getAssignee())).collect(Collectors.toList());
            List<User> users = userBizApi.findUserById(new ArrayList<Long>() {{
                addAll(ids);
            }}).getData();
            if (CollUtil.isNotEmpty(users)) {
                res.forEach(obj -> users.forEach(user -> {
                    if (String.valueOf(user.getId()).equals(obj.getAssignee())) {
                        obj.setCuser(user);
                    }
                }));
            }
        }
        return res;
    }

    private <T extends TaskHiResDTO> T getHiTaskRes(HistoricTaskInstance task) {
        TaskHiResDTO res = new TaskHiResDTO();
        res.setId(task.getId());
        res.setName(task.getName());
        res.setTenantId(task.getTenantId());
        res.setTaskDefKey(task.getTaskDefinitionKey());
        res.setInst(new RemoteData(task.getProcessInstanceId()));
        res.setItem(new RemoteData(task.getId()));
        res.setAssignee(task.getAssignee());
        return (T) res;
    }

    private <T extends TaskResDTO> T getTaskRes(Task task) {
        if (task == null) {
            return null;
        }
        TaskResDTO res = new TaskResDTO();
        res.setId(task.getId());
        res.setName(task.getName());
        res.setTenantId(task.getTenantId());
        res.setIsSuspended(task.isSuspended());
        res.setTaskDefKey(task.getTaskDefinitionKey());
        res.setInst(new RemoteData(task.getProcessInstanceId()));
        res.setAssignee(task.getAssignee());
        return (T) res;
    }
}
