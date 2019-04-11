package com.proper.enterprise.platform.auth.common.filter

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

@Ignore
class AccessTokenFilterTest extends AbstractIntegrationTest {

    @Autowired
    private AccessTokenService service

    @Test
    void accessTest() {
        get('/test', HttpStatus.UNAUTHORIZED)

        def token = service.generate()
        def accessToken = new AccessTokenVO('test', 'for test using', token, 'GET:/test')
        service.saveOrUpdate(accessToken)
        get("/test?access_token=$token", HttpStatus.NOT_FOUND)
        get("/test/notexist?access_token=$token", HttpStatus.FORBIDDEN)
    }

}
