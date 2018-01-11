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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;

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
                builder.addZipInputStream(new ZipInputStream(resource.getInputStream()));
            } else {
                builder.addInputStream(resourceName, resource.getInputStream());
            }
        }
        Deployment deployment = builder.deploy();
        convertToModels(deployment.getId(), resources);
        return deployment;
    }

    /**
     * Determines the name to be used for the provided resource.
     *
     * @param resource the resource to get the name for
     * @return the name of the resource
     */
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

                ObjectNode modelObjectNode = objectMapper.createObjectNode();
                modelObjectNode.put(MODEL_NAME, resource.getFilename());
                modelObjectNode.put(MODEL_REVISION, 1);
                modelObjectNode.put(MODEL_DESCRIPTION, resource.getDescription());

                Model modelData = repositoryService.newModel();
                modelData.setMetaInfo(modelObjectNode.toString());
                modelData.setName(resource.getFilename());
                modelData.setDeploymentId(deploymentId);
                repositoryService.saveModel(modelData);

                ObjectNode modelNode = converter.convertToJson(bpmnModel);
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
