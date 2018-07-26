package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

class VariablesTest extends WorkflowAbstractTest {

    private static final String VARIABLES_WORKFLOW_KEY = 'testVariables'

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void test() {
        setCurrentUserId("admin")
        Map mainForm = new HashMap()
        mainForm.put("a", "a")
        PEPProcInstVO pepProcInst = start(VARIABLES_WORKFLOW_KEY, mainForm)
        Map step1 = getTask("step1")
        Map stepForm = new HashMap()
        stepForm.put("b", "b")
        complete(step1.taskId, stepForm)
        assert findHis(pepProcInst.procInstId).get("ended")
    }

}
