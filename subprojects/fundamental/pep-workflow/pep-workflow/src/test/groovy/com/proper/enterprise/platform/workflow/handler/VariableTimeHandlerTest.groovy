package com.proper.enterprise.platform.workflow.handler

import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class VariableTimeHandlerTest extends WorkflowAbstractTest {

    private static final String TEST_TIME_HANDLER_KEY = "testTimeHandler"

    @Autowired
    TaskService taskService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/adminUsers.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void test() {
        Map map2 = new HashMap()
        Date date = new Date()
        map2.put("date", date.getTime())
        map2.put("date2", "2018-07-23")
        map2.put("date3", "2018-07-23 22:33:44")
        String procInstId2 = start(TEST_TIME_HANDLER_KEY, map2).getProcInstId()
        Task task = taskService.createTaskQuery().includeProcessVariables().singleResult()
        assert "2018-07-23" == task.getProcessVariables().get("a").date2
        assert "2018-07-23 22:33:44" == task.getProcessVariables().get("a").date3
        taskService.complete(task.getId())
        assert DateUtil.toString(date, "yyyy-MM-dd") == Authentication.getCurrentUserId()


    }


    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/adminUsers.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void testOld() {
        Map map2 = new HashMap()
        Date date = new Date()
        map2.put("date", date.getTime())
        map2.put("date2", "2018-07-23")
        map2.put("date3", "2018-07-23 22:33:44")
        String procInstId2 = start("testOldTimeHandler", map2).getProcInstId()
        Task task = taskService.createTaskQuery().includeProcessVariables().singleResult()
        assert "2018-07-23" == task.getProcessVariables().get("a").date2
        assert "2018-07-23 22:33:44" == task.getProcessVariables().get("a").date3
        taskService.complete(task.getId())
        assert DateUtil.toString(date, "yyyy-MM-dd") == Authentication.getCurrentUserId()
    }

}
