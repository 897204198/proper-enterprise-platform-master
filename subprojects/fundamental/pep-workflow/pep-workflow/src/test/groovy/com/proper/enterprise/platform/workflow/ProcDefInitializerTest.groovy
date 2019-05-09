package com.proper.enterprise.platform.workflow

import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.workflow.enums.ProcDefDeployType
import com.proper.enterprise.platform.workflow.service.DeployService
import org.flowable.ui.modeler.domain.AbstractModel
import org.flowable.ui.modeler.domain.Model
import org.flowable.ui.modeler.repository.ModelRepository
import org.flowable.ui.modeler.repository.ModelSort
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProcDefInitializerTest extends AbstractJPATest {

    @Autowired
    ModelRepository modelRepository

    @Autowired
    DeployService deployService

    @Test
    void checkAutoDeployment() {
        assert !queryDeployment().isEmpty()
        assert !queryModel().isEmpty()
    }

    private def queryDeployment() {
        deployService.findByName(ProcDefInitializer.DEPLOY_NAME)
    }

    private def queryModel() {
        modelRepository.findByModelType(AbstractModel.MODEL_TYPE_BPMN, ModelSort.MODIFIED_DESC)
    }

    @Autowired
    ProcDefInitializer pdi

    @Test
    void checkProcDefUpdate() {
        // First deploy when initial procDefInitializer bean
        pdi.workflowProperties.deployType = ProcDefDeployType.OVERRIDE
        // deploy twice
        2.times {
            pdi.init()
            pdi.shutdown()
        }
        def list = queryDeployment()
        assert list.size() == 3
        List<Model> models = modelRepository.findByModelType(AbstractModel.MODEL_TYPE_BPMN, ModelSort.MODIFIED_DESC)
        for (int i = 0; i < list.size(); i++) {
            models[3 * i].comment == list[i].name + list[i].id
        }
        pdi.workflowProperties.deployType = ProcDefDeployType.NONE
        2.times {
            pdi.init()
            pdi.shutdown()
        }
        assert queryDeployment().size() == 3

        pdi.workflowProperties.deployType = ProcDefDeployType.DROP_CREATE
        pdi.init()
        assert queryDeployment().size() == 1
        pdi.shutdown()

        pdi.init()
        pdi.shutdown()
        assert queryDeployment().size() == 0
        assert queryModel().size() == 0
    }

    @Test
    void onlyDeployOnce() {
        // Clean deployments
        pdi.workflowProperties.deployType = ProcDefDeployType.DROP_CREATE
        pdi.shutdown()

        pdi.resetDeployed()
        assert queryDeployment().size() == 0
        pdi.workflowProperties.deployType = ProcDefDeployType.ONCE
        pdi.init()
        assert queryDeployment().size() == 1
        pdi.shutdown()
        assert queryDeployment().size() == 1

        pdi.init()
        assert queryDeployment().size() == 1
    }

}
