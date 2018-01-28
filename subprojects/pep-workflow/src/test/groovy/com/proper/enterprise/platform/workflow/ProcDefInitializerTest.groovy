package com.proper.enterprise.platform.workflow
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.workflow.service.DeployService
import org.flowable.engine.RepositoryService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProcDefInitializerTest extends AbstractTest {

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
        repositoryService.createModelQuery().list()
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
        repositoryService.createProcessDefinitionQuery().deploymentId(list[0].id).list().each {
            assert it.version == 3
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
