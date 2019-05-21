package com.proper.enterprise.platform.workflow.rule

import com.proper.enterprise.platform.workflow.test.WorkflowAbstractTest
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO
import org.junit.Test
import org.springframework.test.context.jdbc.Sql

class CandidateRuleTest extends WorkflowAbstractTest {

    private static final String CANDIDATE_RULE_KEY = "ruleTest"

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql",
        "/com/proper/enterprise/platform/workflow/datadics.sql",
        "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void test() {
        identityService.setAuthenticatedUserId("user1")
        PEPProcInstVO pepProcInstVO = start(CANDIDATE_RULE_KEY, new HashMap<String, Object>())
        setCurrentUserId("user1")
        Map taskUser1 = getTask("checkUserRule")
        complete(taskUser1.get("taskId"), new HashMap<>())
        setCurrentUserId("user2")
        Map taskGroup2 = getTask("checkGroupRule")
        complete(taskGroup2.get("taskId"), new HashMap<>())
        assert isEnded(pepProcInstVO.getProcInstId())
    }
}
