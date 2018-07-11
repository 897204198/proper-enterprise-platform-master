package com.proper.enterprise.platform.core.security

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class AuthenticationTest extends AbstractTest {

    @Test
    void getCurrentUserId() {
        assert PEPConstants.DEFAULT_OPERAOTR_ID == Authentication.getCurrentUserId()
    }

}
