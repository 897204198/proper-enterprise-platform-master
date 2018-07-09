package com.proper.enterprise.platform.auth.jwt.service

import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.service.APISecret
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.auth.service.impl.JWTServiceImpl
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import javax.servlet.http.Cookie

class JWTServiceImplTest extends AbstractTest {

    @Autowired
    private JWTService jwtService

    @Autowired
    private APISecret secret

    @Test
    void testGenerateAndVerifyToken() {
        def header = new JWTHeader()
        header.setId('1')
        header.setName('test')
        def payload = new JWTPayloadImpl()
        payload.setRoles('a,b,c')
        def token = jwtService.generateToken(header, payload)

        assert jwtService.verify(token)
    }

    @Test
    void testMultiTokens() {
        def header1 = new JWTHeader('k1', 't1')
        def payload1 = new JWTPayloadImpl('t1emp')
        def secret1 = secret.getAPISecret(header1.getId())
        def token1 = jwtService.generateToken(header1, payload1)

        def header2 = new JWTHeader('k2', 't2')
        def payload2 = new JWTPayloadImpl('t2emp')
        def secret2 = secret.getAPISecret(header2.getId())
        def token2 = jwtService.generateToken(header2, payload2)

        assert secret1 == secret.getAPISecret(header1.getId())
        assert secret2 == secret.getAPISecret(header2.getId())
        assert secret.getAPISecret(header1.getId()) != secret.getAPISecret(header2.getId())

        assert jwtService.verify(token2)
        assert jwtService.verify(token1)
    }

    @Test
    void getTokenFromMockReq() {
        def token = 'a.b.c'
        mockRequest.addHeader("Authorization", token)
        assert jwtService.getTokenFromHeader(mockRequest) == token
    }

    @Test
    void getTokenFromMockReqWithBearer() {
        def token = 'a.b.c'
        mockRequest.addHeader("Authorization", "Bearer $token")
        assert jwtService.getTokenFromHeader(mockRequest) == token
    }

    @Test
    void getTokenFromReq() {
        def token = 'a.b.c'
        mockRequest.addHeader('Authorization', token)
        assert getAndReturn('/token/get', '', HttpStatus.OK) == token
    }

    @Test
    void verifyInvalidToken() {
        assert !jwtService.verify('')
        assert !jwtService.verify('token')
    }

    @Test
    void getTokenFromCookie() {
        def token = '123456token'
        Cookie cookie = new Cookie(JWTServiceImpl.TOKEN_FLAG, token)
        mockRequest.setCookies(cookie)
        assert token == jwtService.getTokenFromCookie(mockRequest)

        mockRequest.setCookies(new Cookie('test1', 'test1'), new Cookie('test2', 'test2'))
        assert jwtService.getTokenFromCookie(mockRequest) == null
    }

    @Test
    void getTokenFromRequestParam(){
        def token = 'a.b.1'
        mockRequest.setParameter(JWTServiceImpl.TOKEN, token)
        assert jwtService.getTokenFromReqParameter(mockRequest) == token

        def token1 = ''
        mockRequest.setParameter(JWTServiceImpl.TOKEN, token1)
        assert jwtService.getTokenFromReqParameter(mockRequest) == null
    }

}
