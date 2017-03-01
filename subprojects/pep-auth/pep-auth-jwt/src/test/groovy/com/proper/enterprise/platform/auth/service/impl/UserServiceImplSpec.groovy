package com.proper.enterprise.platform.auth.service.impl

import spock.lang.Specification


class UserServiceImplSpec extends Specification {

    def "Get current user without request(always in async tasks) should not throw exception"() {
        expect:
        new UserServiceImpl().getCurrentUser() == null
    }

}
