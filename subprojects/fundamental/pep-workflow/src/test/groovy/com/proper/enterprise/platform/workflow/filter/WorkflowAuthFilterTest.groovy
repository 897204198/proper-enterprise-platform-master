package com.proper.enterprise.platform.workflow.filter

import com.proper.enterprise.platform.test.AbstractIntegrationTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.flowable.engine.IdentityService
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Ignore
class WorkflowAuthFilterTest extends AbstractIntegrationTest {

    @Autowired
    IdentityService identityService

    @Test
    @Sql(["/com/proper/enterprise/platform/workflow/identity.sql", "/com/proper/enterprise/platform/workflow/wfIdmQueryConf.sql"])
    public void currentUserTest() {
        def loginVO = [:]
        loginVO["username"] = "testuser1"
        loginVO["pwd"] = "pwd"
        def token = post("/auth/login", JSONUtil.toJSON(loginVO), HttpStatus.OK).getInputStream().getText()
        get("/test/workflow/?access_token=" + token, HttpStatus.OK)
    }
}
