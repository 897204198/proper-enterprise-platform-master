package com.proper.enterprise.platform.workflow.handler

import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.DateUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.flowable.engine.TaskService
import org.flowable.task.api.Task
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class VariableTimeHandlerTest extends AbstractTest {

    private static final String TEST_TIME_HANDLER_KEY = "testTimeHandler"

    @Autowired
    TaskService taskService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/adminUsers.sql"])
    public void test() {
        Map map = new HashMap()
        map.put("date", DateUtil.toDate("2017-12-11"))
        String procInstId = start(TEST_TIME_HANDLER_KEY, map)
        Task task = taskService.createTaskQuery().singleResult()
        taskService.complete(task.getId())
        assert "2017-12-11" == Authentication.getCurrentUserId()
    }

    private String start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO.getProcInstId()
    }

}
