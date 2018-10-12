package com.proper.enterprise.platform.workflow.api.notice

import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import org.flowable.engine.IdentityService
import org.flowable.engine.TaskService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class TaskCompletedNoticeTest extends WorkflowAbstractTest {
    private static final String TASK_COMPLETED_NOTICE = "taskCompletedNotice"
    private static final String TASK_COMPLETED_NOTICE_TWO_USER = "taskCompletedNoticeTwoUser"
    @Autowired
    private IdentityService identityService

    @Autowired
    private TaskService taskService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/adminUsers.sql"])
    public void testTaskCompletedNotice() {
        identityService.setAuthenticatedUserId("PEP_SYS")
        Authentication.setCurrentUserId("PEP_SYS")
        start(TASK_COMPLETED_NOTICE, new HashMap<String, Object>())
        Map task = getTask("办结通知")
        complete(task.get("taskId").toString(), new HashMap<>())
        assert "PEP_SYS" == Authentication.getCurrentUserId()

        identityService.setAuthenticatedUserId("PEP_SYS")
        Authentication.setCurrentUserId("PEP_SYS")
        start(TASK_COMPLETED_NOTICE_TWO_USER, new HashMap<String, Object>())
        Map task2 = getTask("办结通知2")
        complete(task2.get("taskId").toString(), new HashMap<>())
        assert "超级管理员 start taskCompletedNoticeTwoUser 办结通知2 completed" == Authentication.getCurrentUserId()
    }
}
