package com.tangyh.lamp.activiti.controller.editor;

import cn.hutool.json.JSONObject;
import com.tangyh.basic.base.R;
import com.tangyh.lamp.activiti.exception.MyActivitiExceptionCode;
import com.tangyh.lamp.activiti.exception.MyException;
import com.tangyh.lamp.activiti.service.activiti.MyModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 模型保存
 *
 * @author wz
 * @date 2020-08-07
 */
@RestController
@RequestMapping("service")
@Slf4j
@AllArgsConstructor
public class ModelEditorRestResource implements ModelDataJsonConstants {
    private final MyModelService myModelService;

    /**
     * 保存流程模型
     *
     * @param modelId     模型ID
     * @param name        流程模型名称
     * @param description 描述
     * @param json_xml    流程文件
     * @param svg_xml     图片
     */
    @RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public R<Boolean> saveModel(@PathVariable String modelId
            , String name, String description, String key
            , String json_xml, String svg_xml) {
        myModelService.saveModel(modelId, name, description, key, json_xml, svg_xml);
        return R.success();
    }

    /**
     * 获取流程json信息
     *
     */
    @RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
    public JSONObject getEditorJson(@PathVariable String modelId) {
        return myModelService.getEditorJson(modelId);
    }

    /**
     * 获取流程json文件
     *
     */
    @RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        try {
            return IOUtils.toString(stencilsetStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            MyException.exception(MyActivitiExceptionCode.MODEL_STENCIL_LOAD_ERR);
        }
        return null;
    }
}
