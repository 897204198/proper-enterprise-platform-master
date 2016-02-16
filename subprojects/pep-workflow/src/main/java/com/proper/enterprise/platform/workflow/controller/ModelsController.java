package com.proper.enterprise.platform.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.conf.Constants;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.EditorSource;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.explorer.util.XmlUtil;
import org.activiti.rest.service.api.RestResponseFactory;
import org.activiti.rest.service.api.repository.DeploymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

@RestController
@RequestMapping("/repository/models")
public class ModelsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelsController.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RestResponseFactory restResponseFactory;

    /**
     * According to activiti-webapp-explorer2, an initial editor source is needed when
     * creating a new model. The RESTFul API in activiti-rest module only support a PUT
     * method with multipart/form-data content type request to set editor source for a
     * model, and that API is not easy to call in front-end. This class supply a POST
     * API with nothing else to initial the editor source for an existing model.
     */
    @RequestMapping(value="/{modelId}/source", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void postInitEditorSource(@PathVariable String modelId, @RequestParam String name) {
        repositoryService.addModelEditorSource(modelId,
                EditorSource.initialSource(modelId, name).getBytes(PEPConstants.DEFAULT_CHARSET));
    }

    @RequestMapping(value="/{modelId}/deployment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeploymentResponse deployModel(@PathVariable String modelId, HttpServletResponse response) {
        Model model = repositoryService.getModel(modelId);
        try {
            String processName = model.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService
                    .createDeployment()
                    .name(model.getName())
                    .addString(processName, new String(getModelXml(modelId), Constants.DEFAULT_CHARSET))
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

    @RequestMapping(value = "/{modelId}/export")
    public void exportModel(@PathVariable String modelId, HttpServletResponse response) {
        Model model = repositoryService.getModel(modelId);
        try {
            byte[] bpmnBytes = getModelXml(modelId);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MimeTypeUtils.APPLICATION_XML_VALUE);
            response.setContentLength(bpmnBytes.length);
            response.setHeader("Content-Disposition", "attachment; filename=" + model.getName() + ".bpmn20.xml");
            response.getOutputStream().write(bpmnBytes);
        } catch (IOException e) {
            LOGGER.error("Export model {} error!", model.getName(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public void importModel(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        String filename = file.getOriginalFilename();
        LOGGER.debug("Prepare to import model from file '{}'", filename);

        if (!filename.endsWith(".bpmn20.xml") && !filename.endsWith(".bpmn")) {
            LOGGER.error("Unsupported file type according to filename: {}. ", filename);
            LOGGER.error("Only support *.bpmn20.xml and *.bpmn file types.");
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        try {
            XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
            XMLStreamReader xtr = xif.createXMLStreamReader(file.getInputStream());
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

            if (bpmnModel.getMainProcess() == null
                    || bpmnModel.getMainProcess().getId() == null
                    || bpmnModel.getLocationMap().isEmpty()) {
                LOGGER.error("Invalid BPMN format!");
                response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                return;
            }

            String processName;
            if (StringUtil.isNotNull(bpmnModel.getMainProcess().getName())) {
                processName = bpmnModel.getMainProcess().getName();
            } else {
                processName = bpmnModel.getMainProcess().getId();
            }
            Model modelData = repositoryService.newModel();
            ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processName);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(processName);
            repositoryService.saveModel(modelData);
            LOGGER.debug("Save model {}:{}", modelData.getId(), processName);

            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);

            repositoryService.addModelEditorSource(modelData.getId(),
                    editorNode.toString().getBytes(PEPConstants.DEFAULT_CHARSET));
            LOGGER.debug("Save editor source for model: {}", modelData.getId());
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            LOGGER.error("Import model error!", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
