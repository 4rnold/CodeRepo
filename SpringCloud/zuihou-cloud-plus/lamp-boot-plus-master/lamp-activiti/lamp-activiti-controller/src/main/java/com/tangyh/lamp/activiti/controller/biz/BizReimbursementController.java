package com.tangyh.lamp.activiti.controller.biz;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangyh.basic.annotation.security.PreAuth;
import com.tangyh.basic.base.R;
import com.tangyh.basic.base.controller.SuperController;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.injection.core.InjectionCore;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.basic.utils.BeanPlusUtil;
import com.tangyh.lamp.activiti.domain.core.UpdateCollEntity;
import com.tangyh.lamp.activiti.dto.activiti.InstantSelectReqDTO;
import com.tangyh.lamp.activiti.dto.biz.BizReimbursementPageDTO;
import com.tangyh.lamp.activiti.dto.biz.BizReimbursementResDTO;
import com.tangyh.lamp.activiti.dto.biz.BizReimbursementSaveDTO;
import com.tangyh.lamp.activiti.dto.biz.BizReimbursementUpdateDTO;
import com.tangyh.lamp.activiti.dto.biz.TaskHiReimbursementResDTO;
import com.tangyh.lamp.activiti.dto.biz.TaskReimbursementResDTO;
import com.tangyh.lamp.activiti.entity.biz.BizReimbursement;
import com.tangyh.lamp.activiti.service.activiti.MyTaskService;
import com.tangyh.lamp.activiti.service.biz.BizReimbursementService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 前端控制器
 * 报销流程
 * </p>
 *
 * @author wz
 * @date 2020-08-31
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/bizReimbursement")
@Api(value = "BizReimbursement", tags = "报销流程")
@PreAuth(replace = "bizReimbursement:")
@RequiredArgsConstructor
public class BizReimbursementController extends SuperController<BizReimbursementService, Long, BizReimbursement, BizReimbursementPageDTO, BizReimbursementSaveDTO, BizReimbursementUpdateDTO> {
    private final InjectionCore injectionCore;
    private final MyTaskService myTaskService;

    /**
     * Excel导入后的操作
     *
     */
    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list) {
        List<BizReimbursement> bizReimbursementList = list.stream()
                .map((map) -> BizReimbursement.builder().build()).collect(Collectors.toList());

        return R.success(baseService.saveBatch(bizReimbursementList));
    }


    /**
     * 新增报销流程
     *
     * @param bizReimbursement 新增实体
     */
    @PostMapping("save")
    public R<Boolean> save(@RequestBody BizReimbursement bizReimbursement) {
        boolean tag = baseService.saveBiz(bizReimbursement);
        return R.success(tag);
    }

    /**
     * 删除流程实例及模型
     *
     * @param entity 集合修改实体
     */
    @PostMapping(value = "/delete")
    public R<Boolean> delete(@RequestBody UpdateCollEntity<String> entity) {
        if (CollUtil.isEmpty(entity.getIds())) {
            return R.fail("删除列表为空!");
        }
        Boolean tag = baseService.deleteBiz(entity);
        return R.success(tag);
    }

    /**
     * 请假实例查询
     *
     * @param params 分页模糊查询参数
     */
    @PostMapping(value = "/pageBiz")
    public R<IPage<BizReimbursementResDTO>> pageBiz(@RequestBody PageParams<BizReimbursementPageDTO> params) {
        if (params.getModel().getIsMine()) {
            params.getModel().setCreatedBy(ContextUtil.getUserId());
        }
        IPage<BizReimbursementResDTO> page = baseService.pageBiz(params);
        injectionCore.injection(page, false);
        return R.success(page);
    }

    /**
     * 根据业务id获取实例详情
     *
     * @param id 主键id
     */
    @Override
    @GetMapping(value = "/get")
    public R<BizReimbursement> get(@RequestParam(value = "id") Long id) {
        BizReimbursement entity = baseService.getById(id);
        return R.success(entity);
    }

    /**
     * 当前待办任务
     *
     */
    @PostMapping("pageRunTask")
    public R<IPage<TaskReimbursementResDTO>> pageRunTask(@RequestBody PageParams<InstantSelectReqDTO> dto) {
        dto.getModel().setUserId(ContextUtil.getUserIdStr());
        dto.getModel().setKey(baseService.getKey());
        IPage<TaskReimbursementResDTO> page = BeanPlusUtil.toBeanPage(myTaskService.pageDealtWithRunTasks(dto), TaskReimbursementResDTO.class);
        page.getRecords().forEach(obj -> obj.setBiz(new RemoteData(obj.getInst().getKey())));
        injectionCore.injection(page, false);
        return R.success(page);
    }

    /**
     * 当前待办任务
     *
     */
    @PostMapping("pageHiTask")
    public R<IPage<TaskHiReimbursementResDTO>> pageHiTask(@RequestBody PageParams<InstantSelectReqDTO> dto) {
        dto.getModel().setUserId(ContextUtil.getUserIdStr());
        dto.getModel().setKey(baseService.getKey());
        IPage<TaskHiReimbursementResDTO> page = BeanPlusUtil.toBeanPage(myTaskService.pageDealtWithHiTasks(dto), TaskHiReimbursementResDTO.class);
        page.getRecords().forEach(obj -> obj.setBiz(new RemoteData(obj.getInst().getKey())));
        injectionCore.injection(page, false);
        return R.success(page);
    }
}
