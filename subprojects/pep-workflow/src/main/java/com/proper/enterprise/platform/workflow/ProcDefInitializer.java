package com.proper.enterprise.platform.workflow;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.service.DeployService;
import org.flowable.app.domain.editor.AbstractModel;
import org.flowable.app.service.api.ModelService;
import org.flowable.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

@Component
@Lazy(value = false)
@DependsOn(value = "liquibase")
public class ProcDefInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcDefInitializer.class);

    /**
     * 是否更新流程定义，支持三个值：
     * create-drop：删除之前的部署后重新部署，流程定义版本应该始终为 1
     * true：       不删除之前的部署，直接重新部署，流程定义的版本会随着部署次数增加
     * false：      不进行流程定义的更新
     */
    @Value("${workflow.procDefUpdate}")
    private String procDefUpdate;

    /**
     * Ant 风格的路径匹配，可使用逗号间隔多个路径模式
     */
    @Value("${workflow.procDefLocations}")
    private String procDefLocations;

    @Autowired
    private DeployService deployService;

    @Autowired
    protected ModelService modelService;

    public static final String DEPLOY_NAME = "PEP system processes";

    @PostConstruct
    public void init() throws IOException {
        if ("false".equals(procDefUpdate)) {
            return;
        }
        cleanIfNecessary();
        deployService.deployFromResourcePattern(DEPLOY_NAME, procDefLocations);
    }

    private void cleanIfNecessary() {
        if ("create-drop".equals(procDefUpdate)) {
            List<Deployment> deploymentList = deployService.findByName(DEPLOY_NAME);
            if (CollectionUtil.isNotEmpty(deploymentList)) {
                for (Deployment deployment : deploymentList) {
                    cleanModels(deployment.getId());
                    cleanDeployment(deployment.getId());
                }
                LOGGER.debug("Clean '{}' related deployments and models.", DEPLOY_NAME);
            }
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

}
