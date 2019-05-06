package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.core.i18n.I18NUtil
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class AssgineeCheckTest extends WorkflowAbstractTest {

    private static final String ASSGINEE_CHECK_KEY = 'assigneeCheck'


    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void assgineeCheckTest() {
        setCurrentUserId("admin")
        PEPProcInstVO pepProcInstVO = start(ASSGINEE_CHECK_KEY, new HashMap<String, Object>())
        setCurrentUserId("user1")
        Map task1 = getTask("经办人")
        setCurrentUserId("user2")
        assert I18NUtil.getMessage("workflow.task.complete.no.permissions") == post('/workflow/task/' + task1.taskId, JSONUtil.toJSON(new HashMap()), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
        setCurrentUserId("user1")
        complete(task1.taskId, new HashMap())

        Map taskCandidateUser = getTask("候选人")
        setCurrentUserId("user2")
        assert I18NUtil.getMessage("workflow.task.complete.no.permissions") == post('/workflow/task/' + taskCandidateUser.taskId, JSONUtil.toJSON(new HashMap()), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
        setCurrentUserId("user1")
        complete(taskCandidateUser.taskId, new HashMap())

        Map taskCandidateGroups = getTask("候选用户组")
        setCurrentUserId("user2")
        assert I18NUtil.getMessage("workflow.task.complete.no.permissions") == post('/workflow/task/' + taskCandidateGroups.taskId, JSONUtil.toJSON(new HashMap()), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
        setCurrentUserId("user1")
        complete(taskCandidateGroups.taskId, new HashMap())

        Map taskCandidateRoles = getTask("候选角色")
        setCurrentUserId("user2")
        assert I18NUtil.getMessage("workflow.task.complete.no.permissions") == post('/workflow/task/' + taskCandidateRoles.taskId, JSONUtil.toJSON(new HashMap()), HttpStatus.INTERNAL_SERVER_ERROR)
            .getResponse()
            .getContentAsString()
        setCurrentUserId("user1")
        complete(taskCandidateRoles.taskId, new HashMap())
        assert isEnded(pepProcInstVO.getProcInstId())
    }
}
