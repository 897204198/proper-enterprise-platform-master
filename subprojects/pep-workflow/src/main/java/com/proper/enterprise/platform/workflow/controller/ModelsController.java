package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.workflow.EditorSource;
import com.proper.enterprise.platform.workflow.service.DeployService;
import com.proper.enterprise.platform.workflow.service.PEPModelService;
import com.proper.enterprise.platform.workflow.vo.PEPModelVO;
import org.flowable.app.domain.editor.Model;
import org.flowable.app.model.common.ResultListDataRepresentation;
import org.flowable.app.service.api.ModelService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.ProcessDefinitionQueryProperty;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/repository/models")
public class ModelsController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private DeployService deployService;
    @Autowired
    private PEPModelService pepModelService;
    @Autowired
    private ModelService modelService;

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

    @RequestMapping(value = "/{modelId}/deployment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PEPModelVO> deployModel(@PathVariable String modelId) {
        Deployment deployment = deployService.deployModel(modelId);
        Model model = modelService.getModel(modelId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(model.getKey())
            .orderBy(ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION).desc()
            .list().get(0);
        return responseOfPost(new PEPModelVO(model.getId(), model.getName(), deployment.getDeploymentTime(), processDefinition.getVersion()));
    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResultListDataRepresentation getModels(@RequestParam(required = false) String filter,
                                                  @RequestParam(required = false) String sort, @RequestParam(required = false) Integer modelType) {
        return pepModelService.getModels(filter, sort, modelType);
    }

}
