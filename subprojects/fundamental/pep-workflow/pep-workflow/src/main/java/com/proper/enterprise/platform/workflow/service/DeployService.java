package com.proper.enterprise.platform.workflow.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.AntResourceUtil;
import org.apache.commons.lang3.StringUtils;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.repository.EngineResource;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.common.engine.impl.util.ReflectUtil;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;

import org.flowable.engine.impl.persistence.entity.ResourceEntity;
import org.flowable.engine.impl.persistence.entity.ResourceEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.flowable.ui.common.util.XmlUtil;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class DeployService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployService.class);

    private BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ModelService modelService;

    @Autowired
    private CoreProperties coreProperties;

    /**
     * 流程部署 部署classPath下的流程模板
     *
     * @param name     部署名称
     * @param resource 源路径
     * @return 本次部署信息
     */
    public Deployment deployInClassPath(String name, String resource) {
        DeploymentBuilder builder = repositoryService.createDeployment().name(name);
        builder.addBpmnModel(resource, replaceInitiatorFlag(getBpmnModel(ReflectUtil.getResourceAsStream(resource))));
        return builder.deploy();
    }

    /**
     * 流程部署并将流程导入至流程编辑器  仅提供给流程启动时调用
     *
     * @param name      本次部署名称
     * @param locations 需要部署的流程路径  支持xml,zip,bar,jar
     * @throws IOException io异常
     */
    public void deployFromResourcePattern(String name, String locations) throws IOException {
        DeploymentBuilder builder = repositoryService.createDeployment().name(name);
        Resource[] resources = AntResourceUtil.getResources(locations);
        String resourceName;
        Set<String> fileNameSet = new HashSet<>(resources.length);
        for (Resource resource : resources) {
            resourceName = determineResourceName(resource);
            if (fileNameSet.contains(resource.getFilename())) {
                LOGGER.debug("Ignore duplicated resource {}", resourceName);
                continue;
            } else {
                LOGGER.debug("Loading {} to deploy", resourceName);
                fileNameSet.add(resource.getFilename());
            }
            if (resourceName.endsWith(".bar") || resourceName.endsWith(".zip") || resourceName.endsWith(".jar")) {
                for (ResourceEntity resourceZip : getResourceEntities(resource.getInputStream())) {
                    if (resourceZip.getName().endsWith(".png")) {
                        builder.addBytes(resourceZip.getName(), resourceZip.getBytes());
                        continue;
                    }
                    builder.addBpmnModel(resourceZip.getName(),
                        replaceInitiatorFlag(getBpmnModel(new ByteArrayInputStream(resourceZip.getBytes()))));
                }
            } else {
                builder.addBpmnModel(resourceName, replaceInitiatorFlag(getBpmnModel(resource.getInputStream())));
            }
        }
        Deployment deployment = builder.deploy();
        //导入部署的流程至本地
        importProcessModels(deployment.getName() + deployment.getId(), deployment.getResources());
    }

    /**
     * 根据de_modelId 部署流程
     *
     * @param modelId modelId
     * @return 部署对象
     */
    public Deployment deployModel(String modelId) {
        Model model = modelService.getModel(modelId);
        String resourceName = model.getName() + ".bpmn20.xml";
        BpmnModel bpmnModel = modelService.getBpmnModel(model);
        return repositoryService
            .createDeployment()
            .name(model.getName())
            .addBpmnModel(resourceName, replaceInitiatorFlag(bpmnModel))
            .deploy();
    }

    /**
     * 根据部署名称获得部署列表，按部署时间倒序排列
     *
     * @param name 部署名称
     * @return 部署列表
     */
    public List<Deployment> findByName(String name) {
        return repositoryService.createDeploymentQuery().deploymentName(name).orderByDeploymenTime().desc().list();
    }

    /**
     * 删除流程
     *
     * @param deploymentId 流程部署id
     * @param cascade      是否级联删除
     */
    public void delete(String deploymentId, boolean cascade) {
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    private List<ResourceEntity> getResourceEntities(InputStream inputStreamZip) {
        ZipInputStream zipInputStream = new ZipInputStream(inputStreamZip);
        List<ResourceEntity> resourceEntities = new ArrayList<>();
        try {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    byte[] bytes = IoUtil.readInputStream(zipInputStream, entryName);
                    ResourceEntity resourceZip = new ResourceEntityImpl();
                    resourceZip.setName(entryName);
                    resourceZip.setBytes(bytes);
                    resourceEntities.add(resourceZip);
                }
                entry = zipInputStream.getNextEntry();
            }
            return resourceEntities;
        } catch (Exception e) {
            throw new FlowableException("problem reading zip input stream", e);
        }
    }

    private String determineResourceName(final Resource resource) {
        String resourceName;
        if (resource instanceof ContextResource) {
            resourceName = ((ContextResource) resource).getPathWithinContext();
        } else if (resource instanceof ByteArrayResource) {
            resourceName = resource.getDescription();
        } else {
            try {
                resourceName = resource.getFile().getAbsolutePath();
            } catch (IOException e) {
                resourceName = resource.getFilename();
            }
        }
        return resourceName;
    }

    private void importProcessModels(String deployKey, Map<String, EngineResource> stringEngineResourceMap) {
        for (Map.Entry<String, EngineResource> entry : stringEngineResourceMap.entrySet()) {
            if (!entry.getKey().endsWith(".bpmn") && !entry.getKey().endsWith(".bpmn20.xml")) {
                continue;
            }
            importProcessModel(deployKey, entry.getValue());
        }
    }

    /**
     * Replace $INITIATOR flag if needed
     *
     * @param bpmnModel BPMN model to deploy
     * @return model post process
     */
    private BpmnModel replaceInitiatorFlag(BpmnModel bpmnModel) {
        Map<String, StartEvent> startEventMap = processNoneStartEvents(bpmnModel);
        for (Process process : bpmnModel.getProcesses()) {
            processUserTasks(process.getFlowElements(), process, startEventMap);
        }
        return bpmnModel;
    }

    private BpmnModel getBpmnModel(InputStream inputStream) {
        BpmnModel bpmnModel = null;
        try {
            InputStreamReader in = new InputStreamReader(inputStream, Charset.forName(coreProperties.getCharset()));
            XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
                throw new ErrMsgException("Make sure that the file contains valid BPMN");
            }
            if (bpmnModel.getLocationMap().isEmpty()) {
                throw new ErrMsgException("Make sure that the file contains BPMN DI information");
            }
        } catch (XMLStreamException xse) {
            LOGGER.error("Convert process definition to model error!", xse);
        }
        return bpmnModel;
    }

    private Map<String, StartEvent> processNoneStartEvents(BpmnModel bpmnModel) {
        Map<String, StartEvent> startEventMap = new HashMap<>(16);
        for (Process process : bpmnModel.getProcesses()) {
            for (FlowElement flowElement : process.getFlowElements()) {
                if (flowElement instanceof StartEvent) {
                    StartEvent startEvent = (StartEvent) flowElement;
                    if (org.apache.commons.collections.CollectionUtils.isEmpty(startEvent.getEventDefinitions())) {
                        if (StringUtils.isEmpty(startEvent.getInitiator())) {
                            startEvent.setInitiator("initiator");
                        }
                        startEventMap.put(process.getId(), startEvent);
                        break;
                    }
                }
            }
        }
        return startEventMap;
    }

    private void processUserTasks(Collection<FlowElement> flowElements, Process process, Map<String, StartEvent> startEventMap) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                if ("$INITIATOR".equals(userTask.getAssignee()) && startEventMap.get(process.getId()) != null) {
                    userTask.setAssignee("${" + startEventMap.get(process.getId()).getInitiator() + "}");
                }

            } else if (flowElement instanceof SubProcess) {
                processUserTasks(((SubProcess) flowElement).getFlowElements(), process, startEventMap);
            }
        }
    }

    private void importProcessModel(String deployKey, EngineResource resource) {
        String fileName = resource.getName();
        byte[] bytes = resource.getBytes();
        try {
            BpmnModel bpmnModel = getBpmnModel(new ByteArrayInputStream(bytes));
            Process process = bpmnModel.getMainProcess();
            String name = StringUtils.isNotEmpty(process.getName()) ? process.getName() : process.getId();
            String description = process.getDocumentation();
            ObjectNode modelNode = bpmnJsonConverter.convertToJson(bpmnModel);
            Model newModel = new Model();
            newModel.setName(name);
            newModel.setKey(process.getId());
            newModel.setModelType(AbstractModel.MODEL_TYPE_BPMN);
            newModel.setDescription(description);
            newModel.setModelEditorJson(modelNode.toString());
            newModel.setComment(deployKey);
            modelService.createModel(newModel, simulationCurrentUser());
        } catch (BadRequestException e) {
            throw new ErrMsgException("BadRequest  for " + fileName + ", error message " + e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Import failed for {}", fileName, e);
            throw new ErrMsgException("Import failed for " + fileName + ", error message " + e.getMessage());
        }
    }

    private User simulationCurrentUser() {
        User user = new UserEntityImpl();
        user.setId(coreProperties.getDefaultOperatorId());
        return user;
    }
}
