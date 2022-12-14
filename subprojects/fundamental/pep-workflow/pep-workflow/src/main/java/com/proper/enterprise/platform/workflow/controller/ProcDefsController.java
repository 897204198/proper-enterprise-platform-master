package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.workflow.service.PEPProcDefsService;
import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.ui.common.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "/repository/process-definitions/")
@RequestMapping("/repository/process-definitions/")
public class ProcDefsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger("ProcDefsController");

    private RepositoryService repositoryService;

    private PEPProcDefsService pepProcDefsService;

    @Autowired
    ProcDefsController(RepositoryService repositoryService, PEPProcDefsService pepProcDefsService) {
        this.repositoryService = repositoryService;
        this.pepProcDefsService = pepProcDefsService;
    }

    @RequestMapping(value = "/{processDefinitionId}/diagram-size", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("‍查询缩略图尺寸")
    public Map<String, Integer> getDiagramSize(@PathVariable @ApiParam(value = "‍流程定义Id", required = true) String processDefinitionId) {
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
        } catch (XMLStreamException ex) {
            LOGGER.error("Create XML stream reader error! ", ex);
            return size;
        }
    }

    @RequestMapping(value = "/{processDefinitionId}/diagram", method = RequestMethod.GET)
    @ApiOperation("‍查询缩略图")
    public ResponseEntity<byte[]> getProcessDefinitionDiagram(@PathVariable @ApiParam(value = "‍流程定义Id", required = true)
                                                                  String processDefinitionId) throws IOException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<>(pepProcDefsService.getProcessDefinitionDiagram(processDefinitionId),
            responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{procDefKey}/latest", method = RequestMethod.GET)
    @ApiOperation("‍根据流程定义Key查询对应的最新流程版本")
    public ResponseEntity<PEPProcessDefinitionVO> getLatest(@PathVariable @ApiParam(value = "‍流程定义Key", required = true)
                                                                String procDefKey) {
        return responseOfGet(pepProcDefsService.getLatest(procDefKey));
    }

}
