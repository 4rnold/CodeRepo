package com.tangyh.lamp.activiti.service.activiti;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.utils.StrHelper;
import com.tangyh.lamp.activiti.dto.activiti.ModelPublishReqDTO;
import com.tangyh.lamp.activiti.dto.activiti.ModelSelectReqDTO;
import com.tangyh.lamp.activiti.exception.MyActivitiExceptionCode;
import com.tangyh.lamp.activiti.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.EditorJsonConstants;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.constants.StencilConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 模型业务
 *
 * @author wz
 * @date 2020-08-07
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MyModelService {
    private final RepositoryService repositoryService;

    private final IdentityService identityService;

    private final ProcessEngineFactoryBean processEngine;

    /**
     * 生成一个新model
     */
    @Transactional(rollbackFor = Exception.class)
    public Model getNewModel() {
        return repositoryService.newModel();
    }

    /**
     * 将bpmn流与图片流压缩至zip返回客户端
     *
     * @param inList       输入流集合
     * @param destFileName 返回名称
     * @param response     输出流
     */
    private static void readActivitiZip(List<InputStream> inList, String destFileName, HttpServletResponse response) {
        try {
            String filename = new String((destFileName + ".zip").getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(bos);
            int idx = 0;
            String[] postFix = {".bpmn", ".png"};
            for (InputStream oneFile : inList) {
                if (oneFile != null) {
                    zos.putNextEntry(new ZipEntry(destFileName + postFix[idx]));
                    byte[] bytes = toByteArray(oneFile);
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                    idx++;
                }
            }
            zos.close();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + filename);
            OutputStream os = response.getOutputStream();
            os.write(bos.toByteArray());
            os.close();
        } catch (Exception ex) {
            log.error("Exception", ex);
        }
    }

    /**
     * 流程发布
     *
     * @param dto 发布实体
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> publish(ModelPublishReqDTO dto) {
        Map<String, String> map = new HashMap<>(3);
        String modelId = dto.getModelId();
        String tenant = ContextUtil.getTenant();
        Model model = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(model.getId());
        if (bytes == null) {
            MyException.exception(MyActivitiExceptionCode.MODEL_DATA_NONE);
        }

        try {
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            Deployment deployment = repositoryService.createDeployment()
                    .name(model.getName())
                    .addBpmnModel(model.getKey() + ".bpmn20.xml", bpmnModel)
                    .tenantId(tenant)
                    .key(model.getKey())
                    .enableDuplicateFiltering()
                    .deploy();

            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);
            if (ContextUtil.getUserId() != null) {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
                User user = identityService.newUser(ContextUtil.getUserIdStr());
                user.setFirstName(ContextUtil.getName());
                repositoryService.addCandidateStarterUser(processDefinition.getId(), user.getId());
            }
            map.put("code", "SUCCESS");
        } catch (Exception e) {
            MyException.exception(MyActivitiExceptionCode.MODEL_ERR);
        }
        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteModel(String modelId) {
        Model modelData = repositoryService.getModel(modelId);
        if (null == modelData) {
            MyException.exception(MyActivitiExceptionCode.DATA_NOT_FOUNT);
        }
        repositoryService.deleteModel(modelId);
        return true;
    }

    /**
     * 根据模型id导出xml
     *
     * @param modelId 模型ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void exportXmlByModelId(HttpServletResponse response, String modelId) {
        Model modelData = repositoryService.getModel(modelId);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        try {
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(modelNode);
            export(response, bpmnModel);
        } catch (IOException e) {
            MyException.exception(MyActivitiExceptionCode.EXPORT_ERR);
        }
    }


    /**
     * 资源导出
     *
     * @param bpmnModel BPMN实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void export(HttpServletResponse response, BpmnModel bpmnModel) throws IOException {
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

        // 生成bpmn流
        ByteArrayInputStream in1 = new ByteArrayInputStream(bpmnBytes);

        // 生成图像流
        InputStream in2 = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
                .generateDiagram(bpmnModel, "png",
                        processEngine.getProcessEngineConfiguration().getActivityFontName(),
                        processEngine.getProcessEngineConfiguration().getLabelFontName(),
                        null,
                        processEngine.getProcessEngineConfiguration().getClassLoader());

        readActivitiZip(Arrays.asList(in1, in2), bpmnModel.getMainProcess().getId(), response);
        response.flushBuffer();
    }

    @Transactional(readOnly = true)
    public IPage<Model> pageModel(PageParams<ModelSelectReqDTO> params) {
        ModelSelectReqDTO model = params.getModel();
        ModelQuery modelQuery = repositoryService.createModelQuery();
        modelQuery.orderByLastUpdateTime().desc();
        if (StrUtil.isNotEmpty(model.getName())) {
            modelQuery.modelNameLike(StrHelper.fullLike(model.getName()));
        }
        if (StrUtil.isNotEmpty(model.getKey())) {
            modelQuery.modelKey(model.getKey());
        }
        if (StrUtil.isNotEmpty(ContextUtil.getTenant())) {
            modelQuery.modelTenantId(ContextUtil.getTenant());
        }
        IPage<Model> page = params.buildPage();
        long count = modelQuery.count();
        page.setTotal(count);
        if (count > 0) {
            List<Model> list = modelQuery.listPage((int) page.offset(), (int) page.getSize());
            page.setRecords(list);
        }
        return page;
    }

    /**
     * 创建模型
     */
    @Transactional(rollbackFor = Exception.class)
    public Model create(String name, String key, String description) {
        long count = repositoryService.createModelQuery().modelKey(key).count();
        if (count > 0) {
            MyException.exception(MyActivitiExceptionCode.MODEL_KEY_EXIST);
        }
        Model model = getNewModel();
        JSONObject modelNode = JSONUtil.createObj();
        modelNode.set(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.set(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.set(ModelDataJsonConstants.MODEL_REVISION, 1);
        model.setKey(key);
        model.setName(name);
        model.setTenantId(ContextUtil.getTenant());
        model.setMetaInfo(modelNode.toString());
        model.setVersion(1);
        createObjectNode(model);
        return model;
    }

    @Transactional(readOnly = true)
    public JSONObject getEditorJson(String modelId) {
        JSONObject modelObj = null;
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            if (StrUtil.isNotEmpty(model.getMetaInfo())) {
                modelObj = JSONUtil.parseObj(model.getMetaInfo());
            } else {
                modelObj = JSONUtil.createObj();
                modelObj.set(ModelDataJsonConstants.MODEL_NAME, model.getName());
            }
            modelObj.set(ModelDataJsonConstants.MODEL_ID, model.getId());

            byte[] modelEditorSource = repositoryService.getModelEditorSource(model.getId());
            try {
                JSONObject editorJsonNode = JSONUtil.parseObj(new String(modelEditorSource, StandardCharsets.UTF_8));
                modelObj.set("model", editorJsonNode);
            } catch (Exception e) {
                log.info("解析model失败", e);
                MyException.exception(MyActivitiExceptionCode.MODEL_JSON_CREATE_ERR);
            }
        }
        return modelObj;
    }

    /**
     * 完善模型信息
     *
     * @param model 模型id
     */
    @Transactional(rollbackFor = Exception.class)
    public void createObjectNode(Model model) {
        repositoryService.saveModel(model);
        JSONObject editorNode = JSONUtil.createObj();
        editorNode.set(EditorJsonConstants.EDITOR_STENCIL_ID, "canvas");
        editorNode.set(EditorJsonConstants.EDITOR_SHAPE_ID, "canvas");
        editorNode.set("stencilset", JSONUtil.createObj().set("namespace", "http://b3mn.org/stencilset/bpmn2.0#"));
        JSONObject propertiesNode = JSONUtil.createObj()
                .set(StencilConstants.PROPERTY_PROCESS_ID, model.getKey())
                .set(StencilConstants.PROPERTY_NAME, model.getName());
        editorNode.set(EditorJsonConstants.EDITOR_SHAPE_PROPERTIES, propertiesNode);
        try {
            repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            MyException.exception(MyActivitiExceptionCode.MODEL_EDITOR_CREATE_ERR);
        }
    }

    /**
     * 保存流程模型,用于编辑器修改
     *
     * @param modelId     模型ID
     * @param name        流程模型名称
     * @param description 描述
     * @param jsonXml     流程文件
     * @param svgXml      图片
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveModel(String modelId, String name, String description, String key, String jsonXml, String svgXml) {
        try {
            Model model = repositoryService.getModel(modelId);
            JSONObject object = JSONUtil.parseObj(jsonXml);
            JSONObject properties = object.getJSONObject(EditorJsonConstants.EDITOR_SHAPE_PROPERTIES);
            properties.set(StencilConstants.PROPERTY_NAME, name);
            properties.set(StencilConstants.PROPERTY_DOCUMENTATION, description);
            if (StrUtil.isNotBlank(key)) {
                properties.set(StencilConstants.PROPERTY_PROCESS_ID, key);
                model.setKey(key);
            }

            object.set(EditorJsonConstants.EDITOR_SHAPE_PROPERTIES, properties);

            JSONObject metaObj = JSONUtil.parseObj(model.getMetaInfo());
            metaObj.set(ModelDataJsonConstants.MODEL_NAME, name);
            metaObj.set(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            model.setMetaInfo(metaObj.toString());
            model.setName(name);
            model.setTenantId(ContextUtil.getTenant());
            repositoryService.saveModel(model);

            jsonXml = JSONUtil.toJsonStr(object);
            repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes(StandardCharsets.UTF_8));

            InputStream svgStream = new ByteArrayInputStream(svgXml.getBytes(StandardCharsets.UTF_8));
            TranscoderInput input = new TranscoderInput(svgStream);
            PNGTranscoder transcoder = new PNGTranscoder();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
        } catch (Exception e) {
            MyException.exception(MyActivitiExceptionCode.MODEL_ERR);
        }
    }

    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @Transactional(readOnly = true)
    public void readResource(String processDefinitionId, String resourceName, HttpServletResponse response) {
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        // 通过接口读取
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (IOException e) {
            MyException.exception(MyActivitiExceptionCode.FILE_LOAD_ERR);
        }
    }

    /**
     * 预览流程定义XML
     *
     * @param processDefinitionId 流程定义id
     * @param resourceName        资源名称
     */
    @Transactional(readOnly = true)
    public org.springframework.ui.Model goViewXml(String processDefinitionId, String resourceName, org.springframework.ui.Model model) {
        StringBuilder fileContent = new StringBuilder();
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        org.activiti.engine.repository.ProcessDefinition pd = pdq.processDefinitionId(processDefinitionId).singleResult();

        // 通过接口读取
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        Map<String, String> map = new HashMap<>(3);
        try {
            InputStreamReader read = new InputStreamReader(
                    resourceAsStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                fileContent.append(lineTxt);
                fileContent.append("\n");
            }
            map.put("code", fileContent.toString());
            read.close();
        } catch (IOException e) {
            MyException.exception(MyActivitiExceptionCode.FILE_LOAD_ERR);
        }
        model.addAttribute("pd", map);
        model.addAttribute("fileName", resourceName);
        return model;
    }

    /**
     * InputStream转化为byte[]数组
     *
     * @param input 输入流
     * @return 字节流
     */
    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
