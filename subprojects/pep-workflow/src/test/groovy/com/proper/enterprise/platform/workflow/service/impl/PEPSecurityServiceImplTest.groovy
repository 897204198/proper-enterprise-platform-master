package com.proper.enterprise.platform.workflow.service.impl

import com.proper.enterprise.platform.test.AbstractTest
import org.flowable.app.service.security.SecurityService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class PEPSecurityServiceImplTest extends AbstractTest {
    @Autowired
    private SecurityService securityService

    @Test
    void test() {
        mockUser("pep-sysadmin", "admin", "123456", true)
        expect:
        assert "pep-sysadmin"==securityService.getCurrentFlowableAppUser().getUsername()
    }
}
