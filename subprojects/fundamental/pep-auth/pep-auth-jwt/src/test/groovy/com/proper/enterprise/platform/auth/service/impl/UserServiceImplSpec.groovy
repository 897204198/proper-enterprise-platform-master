package com.proper.enterprise.platform.auth.service.impl

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.security.Authentication
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    def "Get current user without request(always in async tasks) should not throw exception"() {
         Authentication.setCurrentUserId(null)
        expect:
         Authentication.getCurrentUserId() == PEPConstants.DEFAULT_OPERAOTR_ID
    }

}
