package com.proper.enterprise.platform.workflow.frame.listener

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.flowable.engine.TaskService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class SameAssigneeAutoCompleteListenerTest extends AbstractTest {

    @Autowired
    private TaskService taskService

    @Autowired
    private HistoryService historyService

    @Autowired
    private IdentityService identityService

    private static final String SKIP_PROC_KEY = "testSkip"

    @Test
    @Sql("/com/proper/enterprise/platform/workflow/datadics.sql")
    public void sameAssigneeAutoCompleteListenerTest() {
        Authentication.setCurrentUserId("pep-sysadmin")
        identityService.setAuthenticatedUserId("pep-sysadmin")
        Map<String, Object> variables = new HashMap<>()
        variables.put("skip", true)
        String procInsId = start(SKIP_PROC_KEY, variables)
        assert null != historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(procInsId).singleResult().endTime
    }

    private String start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO.getProcInstId()
    }


    private Map getTask(String taskName) {
        DataTrunk dataTrunk = JSONUtil.parse(get('/workflow/task?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        for (Map pepTaskVO : dataTrunk.getData()) {
            if (taskName.equals(pepTaskVO.name)) {
                return pepTaskVO
            }
        }
        return null
    }
}
