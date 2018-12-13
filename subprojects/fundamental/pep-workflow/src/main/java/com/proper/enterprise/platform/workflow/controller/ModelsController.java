package com.proper.enterprise.platform.workflow.controller;

import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.workflow.service.DeployService;
import com.proper.enterprise.platform.workflow.service.PEPModelService;
import com.proper.enterprise.platform.workflow.vo.PEPModelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.flowable.engine.FormService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.ProcessDefinitionQueryProperty;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repository/models")
@Api(tags = "/repository/models")
public class ModelsController extends BaseController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private DeployService deployService;
    @Autowired
    private PEPModelService pepModelService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private FormService formService;

    @RequestMapping(value = "/{modelId}/deployment", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("‍根据模板Id部署流程")
    public ResponseEntity<PEPModelVO> deployModel(@PathVariable @ApiParam(value = "‍模板Id", required = true) String modelId) {
        Deployment deployment = deployService.deployModel(modelId);
        Model model = modelService.getModel(modelId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(model.getKey())
            .orderBy(ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION).desc()
            .list().get(0);
        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        return responseOfPost(new PEPModelVO(model.getId(),
            model.getName(),
            deployment.getDeploymentTime(),
            processDefinition.getVersion(), startFormKey));
    }

    @RequestMapping(value = "/{modelId}", method = RequestMethod.PUT)
    @ApiOperation("‍根据模板Id修改model模型")
    public ResponseEntity<PEPModelVO> changeModel(@PathVariable @ApiParam(value = "‍模板Id", required = true) String modelId,
                                                  @RequestBody PEPModelVO pepModel) {
        pepModel.setId(modelId);
        return responseOfPut(pepModelService.update(pepModel));
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("‍获得流程model集合")
    public ResultListDataRepresentation getModels(@ApiParam(value = "modelKey") String filter,
                                                  @ApiParam(value = "‍排序 ModelSort") String sort,
                                                  @ApiParam(value = "‍流程类型 AbstractModel") Integer modelType) {
        return pepModelService.getModels(filter, sort, modelType);
    }

}
