package com.proper.enterprise.platform.auth.common.filter

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.vo.AccessTokenVO
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class AccessTokenFilterTest extends AbstractTest {

    @Autowired
    private AccessTokenFilter accessTokenFilter

    @Autowired
    private AccessTokenService service

    @Before
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(accessTokenFilter, '/*').build()
    }

    @Test
    void testFilter() {
        coverFilter(accessTokenFilter)
    }

    @Test
    void accessTest() {
        get('/test', HttpStatus.UNAUTHORIZED)

        def token = service.generate()
        def accessToken = new AccessTokenVO('test', 'for test using', token, 'GET:/test')
        accessToken.setId("201808311853")
        service.saveOrUpdate(accessToken)
        get("/test?access_token=$token", HttpStatus.NOT_FOUND)
        get("/test/notexist?access_token=$token", HttpStatus.FORBIDDEN)
        get("/test", HttpStatus.UNAUTHORIZED)
    }

}
