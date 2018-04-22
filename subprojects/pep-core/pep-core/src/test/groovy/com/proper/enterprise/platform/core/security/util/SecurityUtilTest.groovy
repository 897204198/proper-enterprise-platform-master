package com.proper.enterprise.platform.core.security.util

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test

class SecurityUtilTest extends AbstractTest {

    @Test
    void getCurrentUserId() {
        assert "mock"+PEPConstants.DEFAULT_OPERAOTR_ID == SecurityUtil.getCurrentUserId()
        overrideSingleton('securityService', null)
        assert PEPConstants.DEFAULT_OPERAOTR_ID == SecurityUtil.getCurrentUserId()
    }

}
