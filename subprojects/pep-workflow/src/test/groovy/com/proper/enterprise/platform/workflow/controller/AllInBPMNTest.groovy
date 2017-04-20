package com.proper.enterprise.platform.workflow.controller
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.workflow.service.DeployService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class AllInBPMNTest extends AbstractTest {

    @Autowired
    DeployService deployService

    @Test
    public void controllerIsProcess() {
        def myVar = 0
        assert resOfGet("/bpm/script?initVal=$myVar", HttpStatus.OK) == (myVar + 100) * 200 - 50
    }

    @Test
    public void redeployProcessCouldChangeBehavior() {
        def myVar = 10
        assert resOfGet("/bpm/script?initVal=$myVar", HttpStatus.OK) == (myVar + 100) * 200 - 50
        deployService.deploy('redeploy', 'test-v2.bpmn20.xml')
        assert resOfGet("/bpm/script?initVal=$myVar", HttpStatus.OK) == myVar + 100 - 50
    }

}
