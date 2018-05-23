package com.proper.enterprise.platform.auth.jwt.authz

import com.proper.enterprise.platform.api.auth.service.AuthzService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AuthzServiceTest extends AbstractTest {

     @Autowired
     AuthzService service


    @Test
    void  "#method #url should ignore? #result"() {
        expect:
        assert true == service.shouldIgnore('/workflow/service/model/50/json', 'GET', false)
        assert true == service.shouldIgnore('/workflow/service/model/5/0/json', 'GET', false)
        assert true == service.shouldIgnore('/auth/resources', 'GET', false)
        assert true == service.shouldIgnore('/auth/resources?name=1&type=2', 'GET', false)
        assert true == service.shouldIgnore('/auth/resources/1/2/xml', 'GET', false)
        assert false == service.shouldIgnore('/auth/resources/12/xml', 'GET', false)
        assert false == service.shouldIgnore('/auth/resources/1/2/xml', 'POST', false)
        assert true == service.shouldIgnore('/workflow/service/model/3225/json', 'GET', false)
        assert true == service.shouldIgnore('/pep/auth/login', 'POST', true)
        assert false == service.shouldIgnore('/pep/auth/login', 'GET', true)
        assert false == service.shouldIgnore('非法://内容', 'GET', false)
    }

}
