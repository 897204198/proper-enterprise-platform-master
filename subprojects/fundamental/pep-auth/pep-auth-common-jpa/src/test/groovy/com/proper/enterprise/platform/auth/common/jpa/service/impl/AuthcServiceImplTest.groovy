package com.proper.enterprise.platform.auth.common.jpa.service.impl

import com.proper.enterprise.platform.api.auth.service.AuthcService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class AuthcServiceImplTest extends AbstractJPATest {

    @Autowired
    AuthcService authcService

    @Test
    public void getLoginInfo() {
        def map = [:]
        assert authcService.getUsername(map) == null
        assert authcService.getPassword(map) == null

        map = ["username": "abc", "pwd": "123"]
        assert authcService.getUsername(map) == "abc"
        assert authcService.getPassword(map) == "123"
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/common/jpa/users.sql")
    public void test() {
        assert authcService.authenticate('t3', '123456')
        assert !authcService.authenticate('t2', '123')
        assert !authcService.authenticate('abc', '123')
    }

}
