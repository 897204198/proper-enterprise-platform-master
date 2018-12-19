package com.proper.enterprise.platform.workflow.exception

import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class WorkflowExceptionTest extends WorkflowAbstractTest {

    private static final String WORKFLOW_TARGET_EXCEPTION_KEY = "workflowTargetException"

    private static final String WORKFLOW_AUTO_TASK_ERROR = "workflowAutoTaskError"

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql",
        "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void workflowTargetExceptionTest() {
        start(WORKFLOW_TARGET_EXCEPTION_KEY, new HashMap<String, Object>())
        Map task = getTask("step1")
        assert I18NUtil.getMessage("workflow.task.complete.error") == post('/workflow/task/' + task.taskId, JSONUtil.toJSON(new HashMap<String, Object>()),
            HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql",
        "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void workflowAutoTaskErrorTest(){
        start(WORKFLOW_AUTO_TASK_ERROR, new HashMap<String, Object>())
        Map task = getTask("step1")
        assert I18NUtil.getMessage("workflow.task.complete.error") == post('/workflow/task/' + task.taskId, JSONUtil.toJSON(new HashMap<String, Object>()),
            HttpStatus.INTERNAL_SERVER_ERROR).getResponse().getContentAsString()
    }
}
