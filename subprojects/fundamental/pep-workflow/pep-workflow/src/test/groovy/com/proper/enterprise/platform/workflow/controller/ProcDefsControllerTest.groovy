package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.workflow.plugin.service.DeployService
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ProcDefsControllerTest extends WorkflowAbstractTest {

    @Autowired
    private DeployService deployService


    @Test
    public void getLatestTest() {
        PEPProcessDefinitionVO processDefinitionVONull = getLatest("manyFormTTqq")
        assert null == processDefinitionVONull
        deployService.deployInClassPath("test", "test-v2.bpmn20.xml")
        PEPProcessDefinitionVO processDefinitionVO = getLatest("aib")
        assert processDefinitionVO.getName() == "test"
        deployService.deployInClassPath("test2", "test-v2.bpmn20.xml")
        PEPProcessDefinitionVO processDefinitionVO2 = getLatest("aib")
        assert processDefinitionVO2.getVersion() == processDefinitionVO.getVersion() + 1
    }

}
