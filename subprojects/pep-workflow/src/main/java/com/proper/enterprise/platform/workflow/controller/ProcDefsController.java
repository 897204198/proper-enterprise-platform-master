package com.proper.enterprise.platform.workflow.controller;

import org.activiti.app.util.XmlUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/repository/process-definitions/")
public class ProcDefsController {

    private static final Logger LOGGER = LoggerFactory.getLogger("ProcDefsController");

    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping(value = "/{processDefinitionId}/diagram-size", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> getDiagramSize(@PathVariable String processDefinitionId) {
        LOGGER.debug("in getDiagramSize");
        Map<String, Integer> size = new HashMap<String, Integer>(2);
        try {
            ProcessDefinition procDef = repositoryService.getProcessDefinition(processDefinitionId);
            InputStream defStream = repositoryService.getResourceAsStream(procDef.getDeploymentId(), procDef.getResourceName());
            XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
            XMLStreamReader xtr = xif.createXMLStreamReader(defStream);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

            if (!bpmnModel.getFlowLocationMap().isEmpty()) {
                int maxX = 0;
                int maxY = 0;
                for (String key : bpmnModel.getLocationMap().keySet()) {
                    GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(key);
                    double elementX = graphicInfo.getX() + graphicInfo.getWidth();
                    if (maxX < elementX) {
                        maxX = (int) elementX;
                    }
                    double elementY = graphicInfo.getY() + graphicInfo.getHeight();
                    if (maxY < elementY) {
                        maxY = (int) elementY;
                    }
                }
                size.put("width", maxX + 350);
                size.put("height", maxY + 300);
            }
            return size;
        } catch (XMLStreamException ex){
            LOGGER.error("Create XML stream reader error! ", ex);
            return size;
        }
    }

}
