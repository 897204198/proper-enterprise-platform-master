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
import org.springframework.test.context.jdbc.Sql
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
        jwtVerifyFilter.setHasContext(false)
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
        assert resOfGet('/jwt/filter/ignore/method', HttpStatus.OK) == 'success'
        post('/jwt/filter/ignore/method', '', HttpStatus.UNAUTHORIZED)
    }

    @Test
    public void testAnnotationOnType() {
        assert resOfGet('/jwt/filter/ignore/type', HttpStatus.OK) == 'success'
        post('/jwt/filter/ignore/type', '', HttpStatus.CREATED)
        put('/jwt/filter/ignore/type', '', HttpStatus.OK)
        delete('/jwt/filter/ignore/type', HttpStatus.NO_CONTENT)
    }

    @Test
    public void testIgnorePattern() {
        authzService.setIgnorePatterns('PUT:/jwt/filter/ignore/method,*/workflow/*')
        put('/jwt/filter/ignore/method', '', HttpStatus.OK)
        get('/workflow/designer/index.html', HttpStatus.NOT_FOUND)
    }

    @Test
    public void testWithToken() {
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
    public void accessResource() {
        def token = getToken()
        mockRequest.addHeader('Authorization', token)

        get('/jwt/filter/res/nores', HttpStatus.OK)
        get('/jwt/filter/res/nomenu', HttpStatus.OK)
        get('/jwt/filter/res/menures', HttpStatus.UNAUTHORIZED)
    }

}
