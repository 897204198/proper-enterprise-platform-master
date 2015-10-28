package com.proper.enterprise.platform.integration.workflow.activiti

import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.activiti.engine.RepositoryService
import org.activiti.engine.repository.Model
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProcessIntegTest extends AbstractIntegTest {

    @Autowired
    RepositoryService repositoryService

    @Test
    public void listModels() {
        repositoryService.createModelQuery().list().each {
            println it.metaInfo
        }
    }

//    @Test
    public void createModel() {
        Model model = repositoryService.newModel()
        model.setName('Model created by integration test')
        repositoryService.saveModel(model)
    }

}
