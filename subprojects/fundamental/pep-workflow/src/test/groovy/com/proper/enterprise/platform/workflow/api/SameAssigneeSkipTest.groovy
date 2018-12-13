package com.proper.enterprise.platform.workflow.api

import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.flowable.engine.TaskService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class SameAssigneeSkipTest extends WorkflowAbstractTest {

    @Autowired
    private TaskService taskService

    @Autowired
    private HistoryService historyService

    @Autowired
    private IdentityService identityService

    private static final String SKIP_PROC_KEY = "sameAssigneeSkipTest"

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/datadics.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void sameAssigneeSkipOtherInitiatorTest() {
        Authentication.setCurrentUserId("user1")
        identityService.setAuthenticatedUserId("user1")
        Map<String, Object> variables = new HashMap<>()
        PEPProcInstVO pepProcInstVO = start(SKIP_PROC_KEY, variables)
        Authentication.setCurrentUserId("pep-sysadmin")
        Map task1 = getTask("task1")
        post('/workflow/task/' + task1.taskId, JSONUtil.toJSON(new HashMap()), HttpStatus.CREATED)
        Map skipHis1 = findHis(pepProcInstVO.procInstId)
        assert skipHis1.get("hisTasks").size() == 3
        assert skipHis1.get("hisTasks").get(0).name == 'task3'
        assert skipHis1.get("hisTasks").get(0).sameAssigneeSkip
        assert skipHis1.get("hisTasks").get(1).name == 'task2'
        assert skipHis1.get("hisTasks").get(1).sameAssigneeSkip
        assert skipHis1.get("hisTasks").get(2).name == 'task1'
        assert !skipHis1.get("hisTasks").get(2).sameAssigneeSkip
        Authentication.setCurrentUserId("user1")
        Map task4 = getTask("task4")
        Map task4Form = new HashMap()
        task4Form.put("passOrNot", "0")
        complete(task4.taskId, task4Form)
        Authentication.setCurrentUserId("pep-sysadmin")
        Map task3 = getTask("task3")
        complete(task3.taskId, new HashMap())
        Map skipHis2 = findHis(pepProcInstVO.procInstId)
        assert skipHis2.get("hisTasks").size() == 5
        assert skipHis2.get("hisTasks").get(0).name == "task3"
        assert !skipHis2.get("hisTasks").get(0).sameAssigneeSkip
        assert skipHis2.get("hisTasks").get(1).name == "task4"
        assert !skipHis2.get("hisTasks").get(1).sameAssigneeSkip
        assert skipHis2.get("hisTasks").get(4).name == "task1"
        assert !skipHis2.get("hisTasks").get(4).sameAssigneeSkip
        Authentication.setCurrentUserId("user1")
        Map task42 = getTask("task4")
        task4Form.put("passOrNot", "1")
        complete(task42.taskId, task4Form)
        assert null != historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(pepProcInstVO.getProcInstId()).singleResult().endTime
    }
}
