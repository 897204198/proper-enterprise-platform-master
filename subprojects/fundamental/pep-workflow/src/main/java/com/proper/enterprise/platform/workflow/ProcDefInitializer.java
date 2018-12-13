package com.proper.enterprise.platform.workflow;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.enums.ProcDefDeployType;
import com.proper.enterprise.platform.workflow.service.DeployService;
import org.flowable.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.flowable.ui.modeler.domain.AbstractModel;
import org.flowable.ui.modeler.serviceapi.ModelService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

@Component
@Lazy(value = false)
@DependsOn(value = "flowableLiquibase")
public class ProcDefInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcDefInitializer.class);

    private WorkflowProperties workflowProperties;

    private DeployService deployService;

    private ModelService modelService;

    @Autowired
    public ProcDefInitializer(DeployService deployService, ModelService modelService, WorkflowProperties workflowProperties) {
        this.deployService = deployService;
        this.modelService = modelService;
        this.workflowProperties = workflowProperties;
    }

    public static final String DEPLOY_NAME = "PEP system processes";

    @PostConstruct
    public void init() throws IOException {
        if (ProcDefDeployType.NONE.equals(workflowProperties.getDeployType())) {
            return;
        }
        cleanIfNecessary();
        if (hasDeployed() && ProcDefDeployType.ONCE.equals(workflowProperties.getDeployType())) {
            return;
        }
        deployService.deployFromResourcePattern(DEPLOY_NAME, workflowProperties.getDeployLocations());
    }

    private void cleanIfNecessary() {
        if (ProcDefDeployType.DROP_CREATE.equals(workflowProperties.getDeployType())) {
            resetDeployed();
        }
    }

    private void resetDeployed() {
        List<Deployment> deploymentList = deployService.findByName(DEPLOY_NAME);
        if (CollectionUtil.isNotEmpty(deploymentList)) {
            for (Deployment deployment : deploymentList) {
                cleanModels(deployment.getId());
                cleanDeployment(deployment.getId());
            }
            LOGGER.debug("Clean '{}' related deployments and models.", DEPLOY_NAME);
        }
    }

    private void cleanDeployment(String deploymentId) {
        LOGGER.debug("Delete deployment {}", deploymentId);
        deployService.delete(deploymentId, false);
    }

    private void cleanModels(String deploymentId) {
        String deployKey = DEPLOY_NAME + deploymentId;
        List<AbstractModel> models = modelService.getModelsByModelType(AbstractModel.MODEL_TYPE_BPMN);
        for (AbstractModel model : models) {
            if (!deployKey.equals(model.getComment())) {
                continue;
            }
            modelService.deleteModel(model.getId());
        }
    }

    @PreDestroy
    public void shutdown() {
        cleanIfNecessary();
    }

    private boolean hasDeployed() {
        return deployService.findByName(DEPLOY_NAME).size() > 0;
    }

}
