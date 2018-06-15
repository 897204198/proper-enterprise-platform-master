package com.proper.enterprise.platform.auth.jwt.filter

import com.proper.enterprise.platform.api.auth.dao.MenuDao
import com.proper.enterprise.platform.api.auth.dao.ResourceDao
import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.api.auth.service.AuthzService
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class JWTVerifyFilterTest extends AbstractTest {

    @Autowired
    JWTVerifyFilter jwtVerifyFilter

    @Autowired
    AuthzService authzService

    @Autowired
    JWTService jwtService

    @Autowired
    UserDao userDao

    @Autowired
    MenuDao menuDao

    @Autowired
    ResourceDao resourceDao

    @Before
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(jwtVerifyFilter, '/*').build()
    }

    @Test
    void testFilter() {
        coverFilter(jwtVerifyFilter)
    }

    @Test
    void noHandler() {
        get('/jwt/filter/nohandler', HttpStatus.UNAUTHORIZED)
    }

    @Test
    void testAnnotationOnMethod() {
        assert resOfGet('/jwt/filter/ignore/method', HttpStatus.OK) == 'success'
        post('/jwt/filter/ignore/method', '', HttpStatus.UNAUTHORIZED)
    }

    @Test
    void testAnnotationOnType() {
        assert resOfGet('/jwt/filter/ignore/type', HttpStatus.OK) == 'success'
        post('/jwt/filter/ignore/type', '', HttpStatus.CREATED)
        put('/jwt/filter/ignore/type', '', HttpStatus.OK)
        delete('/jwt/filter/ignore/type', HttpStatus.NO_CONTENT)
    }

    @Test
    void testIgnorePattern() {
        put('/jwt/filter/ignore/method', '', HttpStatus.OK)
        get('/workflow/designer/index.html', HttpStatus.NOT_FOUND)
    }

    @Test
    void testWithToken() {
        def token = getToken()
        mockRequest.addHeader('Authorization', token)
        assert resOfGet('/token/get', HttpStatus.OK) == token

        // unauthorized after clear token
        jwtService.clearToken(getHeader())
        assert get('/token/get', HttpStatus.UNAUTHORIZED)
    }

    private String getToken() {
        def header = getHeader()
        def payload = new JWTPayloadImpl()
        payload.setRoles('a,b,c')
        payload.setEmpName('en')
        jwtService.generateToken(header, payload)
    }

    private static JWTHeader getHeader() {
        def header = new JWTHeader()
        header.setId('art')
        header.setName('art')
        header
    }

    @Test
    @Sql
    void accessResource() {
        def token = getToken()
        mockRequest.addHeader('Authorization', token)

        get('/jwt/filter/res/nores', HttpStatus.OK)
        get('/jwt/filter/res/nomenu', HttpStatus.OK)
        get('/jwt/filter/res/menures', HttpStatus.FORBIDDEN)
    }

}
