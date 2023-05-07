package com.tangyh.lamp.activiti.service.activiti;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tangyh.basic.base.request.PageParams;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.utils.BizAssert;
import com.tangyh.basic.utils.StrHelper;
import com.tangyh.lamp.activiti.domain.activiti.ProcessDefinitionDO;
import com.tangyh.lamp.activiti.exception.MyActivitiExceptionCode;
import com.tangyh.lamp.activiti.exception.MyException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 流程定义业务
 *
 * @author wz
 * @date 2020-08-07
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
@RequiredArgsConstructor
public class MyProcessDefinitionService {

    private final RepositoryService repositoryService;

    /**
     * 分页查询流程定义文件
     *
     * @param pageParams 分页入参
     */
    @Transactional(readOnly = true)
    public IPage<ProcessDefinitionDO> listProcessDefinition(PageParams<ProcessDefinitionDO> pageParams) {
        IPage<ProcessDefinitionDO> page = pageParams.buildPage().setRecords(new ArrayList());
        ProcessDefinitionDO processDefinitionDO = pageParams.getModel();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery.orderByProcessDefinitionId().desc().orderByProcessDefinitionVersion().desc();
        if (StrUtil.isNotBlank(processDefinitionDO.getName())) {
            processDefinitionQuery.processDefinitionNameLike(StrHelper.fullLike(processDefinitionDO.getName()));
        }
        if (StrUtil.isNotBlank(processDefinitionDO.getKey())) {
            processDefinitionQuery.processDefinitionKeyLike(StrHelper.fullLike(processDefinitionDO.getKey()));
        }
        if (StrUtil.isNotBlank(processDefinitionDO.getCategory())) {
            processDefinitionQuery.processDefinitionCategoryLike(StrHelper.fullLike(processDefinitionDO.getCategory()));
        }
        if (StrUtil.isNotEmpty(ContextUtil.getTenant())) {
            processDefinitionQuery.processDefinitionTenantId(ContextUtil.getTenant());
        }

        page.setTotal(processDefinitionQuery.count());
        if (page.getTotal() > 0) {
            List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage((int) page.offset(), (int) page.getSize());

            for (ProcessDefinition definition : processDefinitionList) {
                ProcessDefinitionEntityImpl entityImpl = (ProcessDefinitionEntityImpl) definition;
                ProcessDefinitionDO entity = BeanUtil.toBean(definition, ProcessDefinitionDO.class);
                DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery().deploymentId(definition.getDeploymentId());
                if (StrUtil.isNotEmpty(ContextUtil.getTenant())) {
                    deploymentQuery.deploymentTenantId(ContextUtil.getTenant());
                }
                Deployment deployment = deploymentQuery.singleResult();
                entity.setDeploymentId(deployment.getId());
                entity.setDeploymentTime(deployment.getDeploymentTime());
                entity.setDeploymentName(deployment.getName());
                entity.setSuspendState(String.valueOf(entityImpl.getSuspensionState()));
                if (entityImpl.getSuspensionState() == 1) {
                    entity.setSuspendStateName("已激活");
                } else {
                    entity.setSuspendStateName("已挂起");
                }
                page.getRecords().add(entity);
            }
        }
        return page;
    }

    /**
     * 删除流程定义
     *
     * @param deploymentIdsArr 部署id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProcessDeploymentByIds(List<String> deploymentIdsArr) {
        for (String deploymentId : deploymentIdsArr) {
            repositoryService.deleteDeployment(deploymentId, true);
        }
        return true;
    }

    /**
     * 修改流程定义状态
     *
     * @param processDefinitionId 流程定义ID
     * @param suspendState        状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void suspendOrActiveApply(String processDefinitionId, String suspendState) {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        query.processDefinitionId(processDefinitionId);
        if (StrUtil.isNotEmpty(ContextUtil.getTenant())) {
            query.processDefinitionTenantId(ContextUtil.getTenant());
        }
        ProcessDefinition processDefinition = query.singleResult();
        if (processDefinition == null) {
            MyException.exception(MyActivitiExceptionCode.LOGIN_HAVE_NOT_AUTH);
        }

        if ("1".equals(suspendState)) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId);
        } else if ("2".equals(suspendState)) {
            repositoryService.activateProcessDefinitionById(processDefinitionId);
        }
    }

    /**
     * 通过ZIP部署流程定义
     *
     * @param name 文件名
     * @param path 地址
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public String deploymentDefinitionByZip(String name, String path) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(new File(path)))) {
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
            Deployment deployment = deploymentBuilder
                    .tenantId(ContextUtil.getTenant())
                    .addZipInputStream(zis)
                    .name(name)
                    //该函数将DeploymentBuilder isDuplicateFilterEnabled 属性设置为 true, 在部署时会检测已部署的相同文件的最后一条记录，如果内容相同，则不会部署
//                    .enableDuplicateFiltering()
                    .deploy();
            return deployment.getId();
        }

    }

    /**
     * 通过流程定义映射模型
     *
     * @param processDefinitionId 流程定义id
     */
    @Transactional(rollbackFor = Exception.class)
    public Model saveModelByPro(String processDefinitionId) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processDefinitionQuery.processDefinitionId(processDefinitionId);
        if (StrUtil.isNotEmpty(ContextUtil.getTenant())) {
            processDefinitionQuery.processDefinitionTenantId(ContextUtil.getTenant());
        }

        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        BizAssert.notNull(processDefinition, "流程定义不存在");
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in;
        XMLStreamReader xtr = null;
        try {
            in = new InputStreamReader(bpmnStream, StandardCharsets.UTF_8);
            xtr = xif.createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            MyException.exception(MyActivitiExceptionCode.FILE_LOAD_ERR);
        }
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);

        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setCategory(processDefinition.getCategory());
        modelData.setName(processDefinition.getName() + "(映射)");
        modelData.setTenantId(ContextUtil.getTenant());
        modelData.setDeploymentId(processDefinition.getDeploymentId());
        ModelQuery modelQuery = repositoryService.createModelQuery().modelKey(modelData.getKey());
        if (StrUtil.isNotEmpty(ContextUtil.getTenant())) {
            modelQuery.modelTenantId(ContextUtil.getTenant());
        }
        long keyCount = modelQuery.count();
        modelData.setVersion(Convert.toInt(keyCount + 1));

        JSONObject meta = JSONUtil.createObj();
        meta.set(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        meta.set(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
        meta.set(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(meta.toString());

        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes(StandardCharsets.UTF_8));

        return modelData;
    }

    public List<ProcessDefinitionDO> getDefVerHistory(String key) {
        List<ProcessDefinitionDO> res = new ArrayList<>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).list();
        for (ProcessDefinition definition : list) {
            ProcessDefinitionEntityImpl entityImpl = (ProcessDefinitionEntityImpl) definition;
            ProcessDefinitionDO entity = BeanUtil.toBean(definition, ProcessDefinitionDO.class);
            entity.setDeploymentId(definition.getDeploymentId());
            entity.setKey(definition.getKey());
            entity.setVersion(definition.getVersion());
            entity.setSuspendState(String.valueOf(entityImpl.getSuspensionState()));
            if (entityImpl.getSuspensionState() == 1) {
                entity.setSuspendStateName("已激活");
            } else {
                entity.setSuspendStateName("已挂起");
            }
            res.add(entity);
        }

        return res;
    }
}
