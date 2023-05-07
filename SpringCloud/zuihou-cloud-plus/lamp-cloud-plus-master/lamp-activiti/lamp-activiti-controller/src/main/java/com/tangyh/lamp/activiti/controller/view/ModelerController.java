package com.tangyh.lamp.activiti.controller.view;

import cn.hutool.core.util.StrUtil;
import com.tangyh.lamp.activiti.service.activiti.MyModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 视图管理
 *
 * @author wz
 * @date 2020-08-07
 */
@Controller
@Slf4j
@RequestMapping("static")
@RequiredArgsConstructor
public class ModelerController {
    private final RepositoryService repositoryService;
    private final MyModelService myModelService;

    /**
     * 跳转首页
     *
     * @param token        token,该token多用于跨域情况
     * @param modelId      模型id
     */
    @RequestMapping("index")
    public ModelAndView index(ModelAndView modelAndView, String token, String modelId) {
        modelAndView.addObject("token", token);
        if (StrUtil.isNotEmpty(modelId)) {
            modelAndView.setViewName("static/modeler");
            return modelAndView;
        }
        return modelAndView;
    }

    /**
     * 跳转编辑器页面
     */
    @GetMapping("editor")
    public String editor() {
        return "static/modeler";
    }

    /**
     * 撤销流程定义
     *
     * @param modelId 模型ID
     */
    @ResponseBody
    @RequestMapping("/revokePublish")
    public Object revokePublish(String modelId) {
        Map<String, String> map = new HashMap<>(3);
        Model modelData = repositoryService.getModel(modelId);
        if (null != modelData) {
            repositoryService.deleteDeployment(modelData.getDeploymentId(), true);
            map.put("code", "SUCCESS");
        } else {
            map.put("code", "FAILURE");
        }
        return map;
    }

    /**
     * 预览流程定义XML
     *
     * @param processDefinitionId 流程定义id
     * @param resourceName        资源名称
     */
    @GetMapping(value = "/goViewXml")
    public String goViewXml(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName, org.springframework.ui.Model model) {
        myModelService.goViewXml(processDefinitionId, resourceName, model);
        return "static/xmlview";
    }

    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @GetMapping(value = "/readResource")
    public void readResource(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName, HttpServletResponse response) {
        myModelService.readResource(processDefinitionId, resourceName, response);

    }

    /**
     * 根据模型Id导出XML
     */
    @GetMapping(value = "/exportXMLByModelId")
    public void exportXmlByModelId(@RequestParam("modelId") String modelId, HttpServletResponse response) {
        myModelService.exportXmlByModelId(response, modelId);
    }

}
