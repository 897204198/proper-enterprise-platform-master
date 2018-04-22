package com.proper.enterprise.platform.auth.service.impl

import com.proper.enterprise.platform.core.security.service.impl.JWTSecurityServiceImpl
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    def "Get current user without request(always in async tasks) should not throw exception"() {
        expect:
        new JWTSecurityServiceImpl().getCurrentUserId() == null
    }

}
