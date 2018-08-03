package com.proper.enterprise.platform.workflow.test

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

abstract class WorkflowAbstractTest extends AbstractTest {

    @Autowired
    IdentityService identityService

    @Autowired
    HistoryService historyService

    protected void setCurrentUserId(String currentUserId) {
        identityService.setAuthenticatedUserId(currentUserId)
        Authentication.setCurrentUserId(currentUserId)
    }

    protected PEPProcInstVO start(String key, Map<String, Object> form) {
        PEPProcInstVO pepProcInstVO = JSONUtil.parse(post('/workflow/process/' + key, JSONUtil.toJSON(form), HttpStatus.CREATED).getResponse().getContentAsString(), PEPProcInstVO.class)
        return pepProcInstVO
    }

    protected Map getTask(String taskName) {
        DataTrunk dataTrunk = JSONUtil.parse(get('/workflow/task?pageNo=1&pageSize=10', HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        for (Map pepTaskVO : dataTrunk.getData()) {
            if (taskName.equals(pepTaskVO.name)) {
                return pepTaskVO
            }
        }
        return null
    }

    protected void complete(String taskId, Map vars) {
        post('/workflow/task/' + taskId, JSONUtil.toJSON(vars), HttpStatus.CREATED)
    }

    protected Map findHis(String procInstId) {
        Map pepWorkflowPathVO = JSONUtil.parse(get('/workflow/process/' + procInstId + '/path', HttpStatus.OK).getResponse().getContentAsString(), Map.class)
        return pepWorkflowPathVO
    }


    protected List<Map> buildPage(String procInstId) {
        List<Map> pages = JSONUtil.parse(get('/workflow/process/' + procInstId + '/page', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        return pages
    }

    protected List<Map> buildPageTask(String taskId) {
        List<Map> pages = JSONUtil.parse(get('/workflow/task/' + taskId + '/page', HttpStatus.OK).getResponse().getContentAsString(), List.class)
        return pages
    }

    boolean isEnded(String procInstId) {
        return null != historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).finished().singleResult()
    }
}
