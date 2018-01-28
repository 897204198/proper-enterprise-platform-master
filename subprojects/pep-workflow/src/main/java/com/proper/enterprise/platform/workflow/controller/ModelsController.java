package com.proper.enterprise.platform.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.workflow.EditorSource;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.rest.service.api.RestResponseFactory;
import org.flowable.rest.service.api.repository.DeploymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/repository/models")
public class ModelsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelsController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RestResponseFactory restResponseFactory;

    /**
     * According to activiti-webapp-explorer2, an initial editor source is needed when
     * creating a new model. The RESTFul API in activiti-rest module only support a PUT
     * method with multipart/form-data content type request to set editor source for a
     * model, and that API is not easy to call in front-end. This class supply a POST
     * API with nothing else to initial the editor source for an existing model.
     */
    @RequestMapping(value = "/{modelId}/source", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void postInitEditorSource(@PathVariable String modelId, @RequestParam String name, @RequestParam(required = false) String description) {
        repositoryService.addModelEditorSource(modelId,
                EditorSource.initialSource(modelId, name, description).getBytes(PEPConstants.DEFAULT_CHARSET));
    }

    @RequestMapping(value = "/{modelId}/deployment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeploymentResponse deployModel(@PathVariable String modelId, HttpServletResponse response) {
        Model model = repositoryService.getModel(modelId);
        try {
            String processName = model.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService
                    .createDeployment()
                    .name(model.getName())
                    .addString(processName, new String(getModelXml(modelId), PEPConstants.DEFAULT_CHARSET))
                    .deploy();

            response.setStatus(HttpStatus.CREATED.value());
            return restResponseFactory.createDeploymentResponse(deployment);
        } catch (IOException e) {
            LOGGER.error("Deploy model {} error! {}", model.getName(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return null;
        }
    }

    private byte[] getModelXml(String modelId) throws IOException {
        byte[] es = repositoryService.getModelEditorSource(modelId);
        ObjectNode node = (ObjectNode) objectMapper.readTree(es);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(node);
        return new BpmnXMLConverter().convertToXML(bpmnModel);
    }

}
