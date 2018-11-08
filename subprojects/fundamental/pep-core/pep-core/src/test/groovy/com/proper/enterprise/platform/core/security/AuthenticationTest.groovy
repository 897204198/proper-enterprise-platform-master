package com.proper.enterprise.platform.core.security

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AuthenticationTest extends AbstractSpringTest {

    @Autowired
    private CoreProperties coreProperties

    @Test
    void getCurrentUserId() {
        assert coreProperties.getDefaultOperatorId() == Authentication.getCurrentUserId()
    }

}
