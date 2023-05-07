package com.tangyh.lamp.activiti.controller.activiti;

import com.tangyh.basic.base.R;
import com.tangyh.lamp.activiti.dto.biz.TaskLeaveResDTO;
import com.tangyh.lamp.activiti.service.activiti.MyTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务管理
 *
 * @author wz
 * @date 2020-08-07
 */
@Slf4j
@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskBizController {
    private final MyTaskService myTaskService;

    /**
     * 任务转办
     *
     * @param taskId 任务id
     * @param userId 用户id
     */
    @RequestMapping(value = "/updateAssignee", method = RequestMethod.GET)
    public R<Boolean> updateAssignee(@RequestParam(value = "taskId") String taskId, @RequestParam(value = "userId") String userId) {
        Boolean bool = myTaskService.setAssignee(taskId, userId);

        return R.success(bool);
    }

    /**
     * 获取流程当前正执行任务列表
     *
     * @param instId 流程id
     */
    @RequestMapping(value = "/getReadyTaskByInst", method = RequestMethod.GET)
    public R<List<TaskLeaveResDTO>> getReadyTaskByInst(@RequestParam(value = "instId") String instId) {

        return R.success(myTaskService.getReadyTaskByInst(instId));
    }

}
