package com.proper.enterprise.platform.workflow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.AntResourceUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.explorer.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class DeployService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployService.class);
    private static final String MODEL_NAME = "name";
    private static final String MODEL_REVISION = "revision";
    private static final String MODEL_DESCRIPTION = "description";

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    public Deployment deploy(String name, String resource) {
        return repositoryService.createDeployment()
                    .name(name)
                    .addClasspathResource(resource)
                    .deploy();
    }

    public Deployment deployFromResourcePattern(String name, String locations) throws IOException {
        DeploymentBuilder builder = repositoryService.createDeployment().name(name);
        Resource[] resources = AntResourceUtil.getResources(locations);
        for (Resource resource : resources) {
            LOGGER.debug("Loading {} to deploy", resource.getFilename());
            builder.addInputStream(resource.getFilename(), resource.getInputStream());
        }
        Deployment deployment = builder.deploy();
        convertToModels(deployment.getId(), resources);
        return deployment;
    }

    private void convertToModels(String deploymentId, Resource[] resources) throws IOException {
        XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
        BpmnJsonConverter converter = new BpmnJsonConverter();
        try {
            for (Resource resource : resources) {
                InputStreamReader in = new InputStreamReader(resource.getInputStream(), PEPConstants.DEFAULT_CHARSET);
                XMLStreamReader xtr = xif.createXMLStreamReader(in);
                BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
                if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
                    throw new ErrMsgException("Make sure that the file contains valid BPMN");
                }
                if (bpmnModel.getLocationMap().isEmpty()) {
                    throw new ErrMsgException("Make sure that the file contains BPMN DI information");
                }
                ObjectNode modelNode = converter.convertToJson(bpmnModel);
                Model modelData = repositoryService.newModel();

                ObjectNode modelObjectNode = objectMapper.createObjectNode();
                modelObjectNode.put(MODEL_NAME, resource.getFilename());
                modelObjectNode.put(MODEL_REVISION, 1);
                modelObjectNode.put(MODEL_DESCRIPTION, resource.getDescription());
                modelData.setMetaInfo(modelObjectNode.toString());
                modelData.setName(resource.getFilename());
                modelData.setDeploymentId(deploymentId);
                repositoryService.saveModel(modelData);
                repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes(PEPConstants.DEFAULT_CHARSET));
            }
        } catch (XMLStreamException xse) {
            LOGGER.error("Convert process definition to model error!", xse);
        }
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

    public void delete(String id, boolean cascade) {
        repositoryService.deleteDeployment(id, cascade);
    }

}
