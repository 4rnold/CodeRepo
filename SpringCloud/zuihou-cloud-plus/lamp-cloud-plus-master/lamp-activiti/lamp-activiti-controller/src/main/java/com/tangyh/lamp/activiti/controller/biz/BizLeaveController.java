package com.tangyh.lamp.activiti.controller.biz;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangyh.basic.annotation.security.PreAuth;
import com.tangyh.basic.base.R;
import com.tangyh.basic.base.controller.SuperController;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.database.mybatis.conditions.Wraps;
import com.tangyh.basic.injection.core.InjectionCore;
import com.tangyh.basic.model.RemoteData;
import com.tangyh.basic.utils.BeanPlusUtil;
import com.tangyh.lamp.activiti.domain.core.UpdateCollEntity;
import com.tangyh.lamp.activiti.dto.activiti.InstantSelectReqDTO;
import com.tangyh.lamp.activiti.dto.biz.BizLeavePageDTO;
import com.tangyh.lamp.activiti.dto.biz.BizLeaveSaveDTO;
import com.tangyh.lamp.activiti.dto.biz.BizLeaveUpdateDTO;
import com.tangyh.lamp.activiti.dto.biz.TaskHiLeaveResDTO;
import com.tangyh.lamp.activiti.dto.biz.TaskLeaveResDTO;
import com.tangyh.lamp.activiti.entity.biz.BizLeave;
import com.tangyh.lamp.activiti.service.activiti.MyTaskService;
import com.tangyh.lamp.activiti.service.biz.BizLeaveService;
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


/**
 * <p>
 * 前端控制器
 * 请假流程
 * </p>
 *
 * @author wz
 * @date 2020-08-12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/bizLeave")
@Api(value = "BizLeave", tags = "请假流程")
@PreAuth(replace = "bizLeave:")
@RequiredArgsConstructor
public class BizLeaveController extends SuperController<BizLeaveService, Long, BizLeave, BizLeavePageDTO, BizLeaveSaveDTO, BizLeaveUpdateDTO> {

    private final InjectionCore injectionCore;
    private final MyTaskService myTaskService;

    @PostMapping("save")
    public R<Boolean> save(@RequestBody BizLeave bizLeave) {
        boolean tag = baseService.saveBiz(bizLeave);
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
     */
    @PostMapping(value = "/pageBiz")
    public R<IPage<BizLeave>> pageBiz(@RequestBody PageParams<BizLeavePageDTO> params) {
        if (params.getModel().getIsMine()) {
            params.getModel().setCreatedBy(ContextUtil.getUserId());
        }
        IPage<BizLeave> page = params.buildPage();
        BizLeave model = BeanPlusUtil.toBean(params.getModel(), BizLeave.class);
        baseService.page(page, Wraps.lbQ(model).orderByDesc(BizLeave::getCreateTime));
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
    public R<BizLeave> get(@RequestParam(value = "id") Long id) {
        BizLeave entity = baseService.getById(id);
        return R.success(entity);
    }

    /**
     * 当前待办任务
     *
     */
    @PostMapping("pageRunTask")
    public R<IPage<TaskLeaveResDTO>> pageRunTask(@RequestBody PageParams<InstantSelectReqDTO> dto) {
        dto.getModel().setUserId(ContextUtil.getUserIdStr());
        dto.getModel().setKey(baseService.getKey());
        IPage<TaskLeaveResDTO> page = BeanPlusUtil.toBeanPage(myTaskService.pageDealtWithRunTasks(dto), TaskLeaveResDTO.class);
        page.getRecords().forEach(obj -> obj.setBiz(new RemoteData(obj.getInst().getKey())));
        injectionCore.injection(page, false);
        return R.success(page);
    }

    /**
     * 当前待办任务
     *
     */
    @PostMapping("pageHiTask")
    public R<IPage<TaskHiLeaveResDTO>> pageHiTask(@RequestBody PageParams<InstantSelectReqDTO> dto) {
        dto.getModel().setUserId(ContextUtil.getUserIdStr());
        dto.getModel().setKey(baseService.getKey());
        IPage<TaskHiLeaveResDTO> page = BeanPlusUtil.toBeanPage(myTaskService.pageDealtWithHiTasks(dto), TaskHiLeaveResDTO.class);
        page.getRecords().forEach(obj -> obj.setBiz(new RemoteData(obj.getInst().getKey())));
        injectionCore.injection(page, false);
        return R.success(page);
    }
}
