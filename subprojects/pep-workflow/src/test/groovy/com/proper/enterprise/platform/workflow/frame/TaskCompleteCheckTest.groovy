package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.sys.i18n.I18NUtil
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class TaskCompleteCheckTest extends WorkflowAbstractTest {

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
        assert I18NUtil.getMessage("workflow.task.completed") == post('/workflow/task/' + step1.taskId, JSONUtil.toJSON(stepForm), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
        assert I18NUtil.getMessage("workflow.task.not.exist") == post('/workflow/task/12313', JSONUtil.toJSON(stepForm), HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }
}
