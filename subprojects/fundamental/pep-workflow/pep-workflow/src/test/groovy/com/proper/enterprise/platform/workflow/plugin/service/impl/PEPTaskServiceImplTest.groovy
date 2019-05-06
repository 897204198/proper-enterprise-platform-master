package com.proper.enterprise.platform.workflow.plugin.service.impl

import com.proper.enterprise.platform.workflow.plugin.service.PEPTaskService
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class PEPTaskServiceImplTest extends WorkflowAbstractTest {

    @Autowired
    private PEPTaskService pepTaskService

    private static final String VARIABLES_WORKFLOW_KEY = 'testVariables'

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    void testTodoCount() {
        setCurrentUserId("admin")
        Map mainForm = new HashMap()
        mainForm.put("a", "a")
        start(VARIABLES_WORKFLOW_KEY, mainForm)
        assert 1 == getTodoCount()
        assert 1 == Long.valueOf(pepTaskService.getTodoCount("admin"))
        start(VARIABLES_WORKFLOW_KEY, mainForm)
        assert 2 == getTodoCount()
        assert 2 == Long.valueOf(pepTaskService.getTodoCount("admin"))
    }
}
