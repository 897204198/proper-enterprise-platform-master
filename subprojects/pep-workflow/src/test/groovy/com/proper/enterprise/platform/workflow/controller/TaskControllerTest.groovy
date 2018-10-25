package com.proper.enterprise.platform.workflow.controller

import com.proper.enterprise.platform.workflow.service.impl.PEPProcessServiceImpl
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO
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


    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void get() {
        setCurrentUserId("admin")
        Map mainForm = new HashMap()
        mainForm.put("a", "b")
        mainForm.put(PEPProcessServiceImpl.FORM_TODO_DISPLAY_FIELDS_KEY, "c")
        PEPProcInstVO pepProcInst = start("autoArchive", mainForm)
        Map task = getTask("form1")
        assert task.form.formData.a == "b"
        assert task.form.formData.(PEPProcessServiceImpl.FORM_TODO_DISPLAY_FIELDS_KEY) == null
        assert task.globalData.(PEPProcessServiceImpl.FORM_TODO_DISPLAY_FIELDS_KEY) == "c"
    }

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    void findTaskAssigneeIsMe() {
        setCurrentUserId("admin")
        Map mainForm = new HashMap()
        mainForm.put("a", "b")
        mainForm.put(PEPProcessServiceImpl.FORM_TODO_DISPLAY_FIELDS_KEY, "c")
        PEPProcInstVO pepProcInst = start("autoArchive", mainForm)
        Map task = getTask("form1")
        assert task.form.formData.a == "b"
        assert task.form.formData.(PEPProcessServiceImpl.FORM_TODO_DISPLAY_FIELDS_KEY) == null
        assert task.globalData.(PEPProcessServiceImpl.FORM_TODO_DISPLAY_FIELDS_KEY) == "c"

        complete(task.taskId, mainForm)
        List<PEPTaskVO> assigneeIsMe = findTaskAssigneeIsMePagination("autoArchive", 1, 10)
        assert assigneeIsMe.size() == 1
    }
}
