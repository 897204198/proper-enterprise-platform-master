package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

class TaskControllerTest extends WorkflowAbstractTest {

    private static final String VARIABLES_WORKFLOW_KEY = 'testVariables'

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void todoCountTest() {
        setCurrentUserId("admin")
        Map mainForm = new HashMap()
        mainForm.put("a", "a")
        PEPProcInstVO pepProcInst = start(VARIABLES_WORKFLOW_KEY, mainForm)
        assert 1 == getTodoCount()
        start(VARIABLES_WORKFLOW_KEY, mainForm)
        assert 2 == getTodoCount()
    }
}
