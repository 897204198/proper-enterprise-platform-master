package com.proper.enterprise.platform.workflow.frame

import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import com.proper.enterprise.platform.workflow.vo.enums.ShowType
import org.flowable.engine.IdentityService
import org.flowable.engine.RepositoryService
import org.flowable.engine.repository.ProcessDefinition
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class SubProseccTest extends WorkflowAbstractTest {

    private static final String SUBPROCESS_WORKFLOW_MAIN_KEY = 'subProcessMain'

    private static final String SUBPROCESS_WORKFLOW_SUB_KEY = 'subProcess1'

    @Autowired
    IdentityService identityService

    @Autowired
    RepositoryService repositoryService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql"])
    public void test() {
        ProcessDefinition processDefinition = repositoryService
            .createProcessDefinitionQuery()
            .processDefinitionKey(SUBPROCESS_WORKFLOW_SUB_KEY)
            .latestVersion()
            .singleResult();
        Authentication.setCurrentUserId("admin")
        identityService.setAuthenticatedUserId("admin")
        Map<String, Object> mainForm = new HashMap<>()
        mainForm.put("a", "a")
        PEPProcInstVO procInst = start(SUBPROCESS_WORKFLOW_MAIN_KEY, mainForm)
        Map step1 = getTask("step1")
        mainForm.put("a", "a1")
        complete(step1.taskId, mainForm)
        Map subStep1 = getTask("subStep1")
        Map pepWorkflowPathVO = findHis(procInst.procInstId)
        assert pepWorkflowPathVO.get("currentTasks").size() == 1

        List<Map> hisPages = buildPage(procInst.getProcInstId())
        assert hisPages.size() == 1
        assert hisPages.get(0).showType == ShowType.SHOW.name()
        assert hisPages.get(0).formData.a == "a1"
        List<Map> taskPages = buildPageTask(subStep1.taskId)
        assert taskPages.size() == 2
        assert taskPages.get(1).showType == ShowType.EDIT.name()
        assert taskPages.get(1).formData == null
        Map<String, Object> subStep1Form = new HashMap<>()
        subStep1Form.put("b", "b")
        complete(subStep1.taskId, subStep1Form)
        List<Map> hisPages2 = buildPage(procInst.getProcInstId())
        assert hisPages2.size() == 2
        assert hisPages2.get(0).showType == ShowType.SHOW.name()
        assert hisPages2.get(0).formData.a == "a1"
        assert hisPages2.get(1).showType == ShowType.SHOW.name()
        assert hisPages2.get(1).formData.b == "b"
        Map endPath = findHis(procInst.procInstId)
        assert endPath.get("ended")
        assert endPath.get("hisTasks").size() == 2
        assert endPath.get("hisTasks").get(0).form.formData.b == "b"
        assert endPath.get("hisTasks").get(1).form.formData.a == "a1"
    }
}
