package com.proper.enterprise.platform.workflow.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ModelSupplement {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RepositoryService repositoryService;

    // TODO
    @RequestMapping(value="/repository/models/{modelId}/deploy", method = RequestMethod.POST)
    public void deployModel(@PathVariable String modelId, HttpServletResponse response) {
        Model model = repositoryService.getModel(modelId);
        byte[] es = repositoryService.getModelEditorSource(modelId);
        ObjectNode node;
        try {
            node = (ObjectNode) objectMapper.readTree(es);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(node);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

            String processName = model.getName() + ".bpmn20.xml";
            repositoryService.createDeployment()
                    .name(model.getName())
                    .addString(processName, new String(bpmnBytes))
                    .deploy();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            es = null;
        }
        response.setStatus(HttpStatus.OK.value());
    }

}
