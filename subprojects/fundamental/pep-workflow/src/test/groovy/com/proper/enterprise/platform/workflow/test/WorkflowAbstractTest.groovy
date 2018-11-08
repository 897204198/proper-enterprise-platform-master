package com.proper.enterprise.platform.workflow.test

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.StringUtil
import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.test.utils.JSONUtil
import com.proper.enterprise.platform.workflow.vo.PEPExtFormVO
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import com.proper.enterprise.platform.workflow.vo.PEPProcessDefinitionVO
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO
import com.proper.enterprise.platform.workflow.vo.PEPWorkflowPageVO
import org.flowable.engine.HistoryService
import org.flowable.engine.IdentityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

abstract class WorkflowAbstractTest extends AbstractJPATest {

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


    protected List<PEPExtFormVO> buildPage(String procInstId) {
        PEPWorkflowPageVO pages = JSONUtil.parse(get('/workflow/process/' + procInstId + '/page', HttpStatus.OK).getResponse().getContentAsString(), PEPWorkflowPageVO.class)
        return pages.getForms()
    }

    protected PEPWorkflowPageVO buildPageHaveGlobalVars(String procInstId) {
        return JSONUtil.parse(get('/workflow/process/' + procInstId + '/page', HttpStatus.OK).getResponse().getContentAsString(), PEPWorkflowPageVO.class)
    }

    protected List<PEPExtFormVO> buildPageTask(String taskId) {
        PEPWorkflowPageVO pages = JSONUtil.parse(get('/workflow/task/' + taskId + '/page', HttpStatus.OK).getResponse().getContentAsString(), PEPWorkflowPageVO.class)
        return pages.getForms()
    }

    protected PEPProcessDefinitionVO getLatest(String procDefKey) {
        String req = get('/repository/process-definitions/' + procDefKey + '/latest',
            HttpStatus.OK).getResponse().getContentAsString()
        if (StringUtil.isEmpty(req)) {
            return null
        }
        PEPProcessDefinitionVO processDefinitionVO = JSONUtil.parse(req, PEPProcessDefinitionVO.class)
        return processDefinitionVO
    }

    protected Long getTodoCount() {
        return Long.valueOf(get('/workflow/task/todo/count', HttpStatus.OK).getResponse().getContentAsString())
    }

    boolean isEnded(String procInstId) {
        return null != historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).finished().singleResult()
    }

    protected List<PEPTaskVO> findTaskAssigneeIsMePagination(String processDefinitionKey, Integer pageNo, Integer pageSize) {
        DataTrunk<PEPTaskVO> dataTrunk = JSONUtil.parse(get('/workflow/task/assignee?processDefinitionName=' + processDefinitionKey + '&pageNo=' + pageNo + '&pageSize=' + pageSize, HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        return dataTrunk.getData()
    }
}
