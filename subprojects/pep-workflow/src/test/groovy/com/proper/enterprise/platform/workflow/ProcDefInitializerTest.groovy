package com.proper.enterprise.platform.workflow

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.workflow.service.DeployService
import org.flowable.app.domain.editor.AbstractModel
import org.flowable.app.domain.editor.Model
import org.flowable.app.repository.editor.ModelRepository
import org.flowable.app.repository.editor.ModelSort
import org.flowable.engine.RepositoryService
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ProcDefInitializerTest extends AbstractTest {

    @Autowired
    ModelRepository modelRepository

    @Autowired
    RepositoryService repositoryService

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
        pdi.procDefUpdate = 'true'
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
        pdi.procDefUpdate = 'false'
        2.times {
            pdi.init()
            pdi.shutdown()
        }
        assert queryDeployment().size() == 3

        pdi.procDefUpdate = 'create-drop'
        pdi.init()
        assert queryDeployment().size() == 1
        pdi.shutdown()

        pdi.init()
        pdi.shutdown()
        assert queryDeployment().size() == 0
        assert queryModel().size() == 0
    }
}
