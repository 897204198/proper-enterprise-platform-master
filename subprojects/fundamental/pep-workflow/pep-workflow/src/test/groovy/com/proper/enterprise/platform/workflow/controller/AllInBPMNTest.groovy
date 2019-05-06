package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.workflow.plugin.service.DeployService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class AllInBPMNTest extends AbstractJPATest {

    @Autowired
    DeployService deployService

    @Test
    void controllerIsProcess() {
        def myVar = 0
        assert resOfGet("/bpm/script?initVal=$myVar", HttpStatus.OK) == (myVar + 100) * 200 - 50
    }

    @Test
    void redeployProcessCouldChangeBehavior() {
        def myVar = 10
        assert resOfGet("/bpm/script?initVal=$myVar", HttpStatus.OK) == (myVar + 100) * 200 - 50
        deployService.deployInClassPath('redeploy', 'test-v2.bpmn20.xml')
        assert resOfGet("/bpm/script?initVal=$myVar", HttpStatus.OK) == myVar + 100 - 50
    }

    @Test
    void controllerIsProcess1() {
        def myVar = 0
        assert resOfGet("/bpm/script1?initVal=$myVar", HttpStatus.OK) == ""
    }

}
