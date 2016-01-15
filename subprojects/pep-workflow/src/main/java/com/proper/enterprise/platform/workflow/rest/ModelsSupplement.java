package com.proper.enterprise.platform.workflow.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.core.PEPConstants;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ModelsSupplement {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelsSupplement.class);

    private static final String INIT_EDITOR_SOURCE =
            "{\"id\":\"canvas\",\"resourceId\":\"canvas\",\"stencilset\":{\"namespace\":\"http://b3mn.org/stencilset/bpmn2.0#\"}}";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RepositoryService repositoryService;

    /**
     * According to activiti-webapp-explorer2, an initial editor source is needed when
     * creating a new model. The RESTFul API in activiti-rest module only support a PUT
     * method with multipart/form-data content type request to set editor source for a
     * model, and that API is not easy to call in front-end. This class supply a POST
     * API with nothing else to initial the editor source for an existing model.
     */
    @RequestMapping(value="/repository/models/{modelId}/source", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void postInitEditorSource(@PathVariable String modelId) {
        repositoryService.addModelEditorSource(modelId, INIT_EDITOR_SOURCE.getBytes(PEPConstants.DEFAULT_CHARSET));
    }

    @RequestMapping(value="/repository/models/{modelId}/deployment", method = RequestMethod.POST)
    public void deployModel(@PathVariable String modelId, HttpServletResponse response) {
        Model model = repositoryService.getModel(modelId);
        byte[] es = repositoryService.getModelEditorSource(modelId);
        try {
            ObjectNode node = (ObjectNode) objectMapper.readTree(es);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(node);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            String processName = model.getName() + ".bpmn20.xml";
            repositoryService.createDeployment()
                    .name(model.getName())
                    .addString(processName, new String(bpmnBytes))
                    .deploy();

            response.setStatus(HttpStatus.CREATED.value());
        } catch (Exception e) {
            LOGGER.error("Deploy model {} error: ", model.getName(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
