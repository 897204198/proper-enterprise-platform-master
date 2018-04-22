package com.proper.enterprise.platform.workflow.service.impl

import com.proper.enterprise.platform.test.AbstractTest
import org.flowable.app.service.security.SecurityService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

class PEPSecurityServiceImplTest extends AbstractTest {
    @Autowired
    private SecurityService securityService

    @Test
    @Sql("/com/proper/enterprise/platform/workflow/adminUsers.sql")
    void test() {
        def token = mockLogin('admin', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        mockRequest.addHeader("Authorization", token)
        expect:
        assert "PEP_SYS"==securityService.getCurrentFlowableAppUser().getUsername()
    }

    private String mockLogin(String user, String pwd, MediaType produce, HttpStatus statusCode) {
        return post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode).getResponse().getContentAsString()
    }
}
