package com.tangyh.lamp.activiti.controller.activiti;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangyh.basic.base.R;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.lamp.activiti.constant.PathProperties;
import com.tangyh.lamp.activiti.domain.activiti.ProcessDefinitionDO;
import com.tangyh.lamp.activiti.service.activiti.MyProcessDefinitionService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.repository.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static com.tangyh.basic.base.R.fail;
import static com.tangyh.basic.base.R.success;

/**
 * 流程定义管理
 *
 * @author wz
 * @date 2020-08-07
 */
@Slf4j
@RestController
@RequestMapping("definition")
@AllArgsConstructor
public class DefinitionBizController {

    private final MyProcessDefinitionService myProcessDefinitionService;
    private final PathProperties pathProperties;

    /**
     * 模型分页查询
     *
     * @param dto 分页查询实体
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public R<IPage<ProcessDefinitionDO>> page(@RequestBody PageParams<ProcessDefinitionDO> dto) {
        return success(myProcessDefinitionService.listProcessDefinition(dto));
    }

    /**
     * 查询某个流程部署key的历史版本
     *
     */
    @DeleteMapping(value = "/findDefVerHistory")
    public R<List<ProcessDefinitionDO>> findDefVerHistory(@RequestParam(value = "key") String key) {
        return R.success(myProcessDefinitionService.getDefVerHistory(key));
    }

    /**
     * 删除流程实例及模型
     *
     */
    @DeleteMapping(value = "/delete")
    public R<Boolean> delete(@RequestParam(value = "deploymentIds[]") List<String> deploymentIds) {
        return R.success(myProcessDefinitionService.deleteProcessDeploymentByIds(deploymentIds));
    }

    /**
     * 修改流程状态
     *
     * @param processDefinitionId 流程定义id
     * @param suspendState        修改状态
     */
    @GetMapping(value = "/updateSuspendState")
    public R<Boolean> updateSuspendState(@RequestParam(value = "id") String processDefinitionId, @RequestParam(value = "suspendState") String suspendState) {
        myProcessDefinitionService.suspendOrActiveApply(processDefinitionId, suspendState);
        return success(true);
    }

    /**
     * 导入流程模型
     *
     * @param file 上传文件
     */
    @SneakyThrows
    @PostMapping(value = "/upload")
    public R<String> upload(@RequestParam(value = "file", required = false) MultipartFile file,
                            @RequestParam(value = "name", required = false) String name) {
        if (file == null || file.isEmpty()) {
            return fail("文件为空!");
        }
        int begin = file.getOriginalFilename().indexOf(".");
        int last = file.getOriginalFilename().length();
        String type = file.getOriginalFilename().substring(begin, last).toLowerCase();
        if (!".zip".equals(type)) {
            return fail("只允许上传ZIP文件!");
        }
        String filePath = pathProperties.getUploadPath();
        String tempStoragePath = Paths.get(filePath, ContextUtil.getTenant(), UUID.fastUUID().toString(true) + ".zip").toString();
        File desc = new File(tempStoragePath);
        FileUtil.writeFromStream(file.getInputStream(), desc);

        if (StringUtils.isEmpty(name)) {
            name = file.getOriginalFilename();
        }

        String id = myProcessDefinitionService.deploymentDefinitionByZip(name, tempStoragePath);
        return success(id);

    }

    /**
     * 通过流程定义映射模型
     *
     * @param processDefinitionId 流程id
     */
    @GetMapping(value = "/saveModelByPro")
    public R<Model> saveModelByPro(@RequestParam(value = "processDefinitionId") String processDefinitionId) {
        Model model = myProcessDefinitionService.saveModelByPro(processDefinitionId);
        return success(model);
    }
}
