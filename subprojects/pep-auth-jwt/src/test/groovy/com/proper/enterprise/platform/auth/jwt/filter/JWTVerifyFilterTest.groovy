package com.proper.enterprise.platform.auth.jwt.filter
import com.proper.enterprise.platform.auth.jwt.authz.AuthzService
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.jwt.service.JWTService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class JWTVerifyFilterTest extends AbstractTest {

    @Autowired
    JWTVerifyFilter jwtVerifyFilter

    @Autowired
    AuthzService authzService

    @Autowired
    JWTService jwtService

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(jwtVerifyFilter, '/*').build()
    }

    @Test
    public void testFilter() {
        coverFilter(jwtVerifyFilter)
    }

    @Test
    public void noHandler() {
        get('/jwt/filter/nohandler', HttpStatus.UNAUTHORIZED)
    }

    @Test
    public void testAnnotationOnMethod() {
        assert getAndReturn('/jwt/filter/ignore/method', '', HttpStatus.OK) == 'success'
        post('/jwt/filter/ignore/method', '', HttpStatus.UNAUTHORIZED)
    }

    @Test
    public void testAnnotationOnType() {
        assert getAndReturn('/jwt/filter/ignore/type', '', HttpStatus.OK) == 'success'
        post('/jwt/filter/ignore/type', '', HttpStatus.CREATED)
        put('/jwt/filter/ignore/type', '', HttpStatus.OK)
        delete('/jwt/filter/ignore/type', HttpStatus.NO_CONTENT)
    }

    @Test
    public void testIgnorePattern() {
        authzService.setIgnorePatterns('PUT:/jwt/filter/ignore/method')
        jwtVerifyFilter.setHasContext(false)
        put('/jwt/filter/ignore/method', '', HttpStatus.OK)
    }

    @Test
    public void testWithToken() {
        def header = new JWTHeader()
        header.setId('1')
        header.setName('test')
        def payload = new JWTPayloadImpl()
        payload.setRoles('a,b,c')
        payload.setEmpName('en')
        def token = jwtService.generateToken(header, payload)
        mockRequest.addHeader('Authorization', token)
        assert getAndReturn('/token/get', '', HttpStatus.OK) == token

        // unauthorized after clear token
        jwtService.clearToken(header)
        assert get('/token/get', HttpStatus.UNAUTHORIZED)
    }

}
