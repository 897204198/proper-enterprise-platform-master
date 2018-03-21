package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
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
    public ResultListDataRepresentation getModels(String filter, String sort, Integer modelType) {
        return pepModelService.getModels(filter, sort, modelType);
    }

}
