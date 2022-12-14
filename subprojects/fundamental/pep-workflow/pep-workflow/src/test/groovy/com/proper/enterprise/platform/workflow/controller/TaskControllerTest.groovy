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
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
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
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
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
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
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
        assert assigneeIsMe.get(0).form.formData.a == "b"

        Map task12 = getTask("form12")
        mainForm.put("a", ["id":"2eb8087a-e0f8-4a5a-85a8-5bf0b2657e35","text":"?????????","code":"currentDept"])
        mainForm.put("a1", "a12")
        mainForm.put("a12", "a12")
        complete(task12.taskId, mainForm)

        assigneeIsMe = findTaskAssigneeIsMePagination("autoArchive", 1, 10)
        assert assigneeIsMe.size() == 2
        assert assigneeIsMe.get(0).form.formData.a.text == "?????????"
        assert assigneeIsMe.get(1).form.formData.a == "b"
        assert null != assigneeIsMe.get(1).globalData
    }
}
