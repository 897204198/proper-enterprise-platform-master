package com.proper.enterprise.platform.auth.jwt.filter

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class AccessTokenFilterTest extends AbstractIntegrationTest {

    @Autowired
    private JWTService jwtService

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
        assert resOfGet(appendSuffix('/token/get', token), HttpStatus.OK) == token

        // unauthorized after clear token
        jwtService.clearToken(getHeader())
        get(appendSuffix('/token/get', token), HttpStatus.UNAUTHORIZED)
    }

    private String getToken() {
        def header = getHeader()
        def payload = new JWTPayloadImpl()
        payload.setName('en')
        jwtService.generateToken(header, payload)
    }

    private static JWTHeader getHeader() {
        def header = new JWTHeader()
        header.setId('art')
        header.setName('art')
        header
    }

    private static def appendSuffix(url, token) {
        "$url?access_token=$token"
    }

    @Test
    @Sql
    void accessResource() {
        def token = getToken()

        get(appendSuffix('/jwt/filter/res/nores', token), HttpStatus.OK)
        get(appendSuffix('/jwt/filter/res/nomenu', token), HttpStatus.OK)
        get(appendSuffix('/jwt/filter/res/menures', token), HttpStatus.FORBIDDEN)
    }

}
