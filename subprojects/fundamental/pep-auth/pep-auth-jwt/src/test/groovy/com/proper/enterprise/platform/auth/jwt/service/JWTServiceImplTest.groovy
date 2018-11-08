package com.proper.enterprise.platform.auth.jwt.service

import com.proper.enterprise.platform.api.auth.service.AccessTokenService
import com.proper.enterprise.platform.auth.common.jpa.entity.AccessTokenEntity
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.service.APISecret
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.core.exception.ErrMsgException
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

import javax.servlet.http.Cookie

class JWTServiceImplTest extends AbstractJPATest {

    @Autowired
    private JWTService jwtService

    @Autowired
    private APISecret secret

    @Autowired
    private AccessTokenService accessTokenService

    @Test
    void testGenerateAndVerifyToken() {
        def header = new JWTHeader()
        header.setId('1')
        header.setName('test')
        def payload = new JWTPayloadImpl()
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
        assert accessTokenService.getToken(mockRequest).value == token
    }

    @Test
    void getTokenFromMockReqWithBearer() {
        def token = 'a.b.c'
        mockRequest.addHeader("Authorization", "Bearer $token")
        assert accessTokenService.getToken(mockRequest).value == token
    }

    @Test
    void getTokenFromReq() {
        def token = 'a.b.c'
        mockRequest.addHeader('Authorization', token)
        assert resOfGet('/token/get',HttpStatus.OK) == token
    }

    @Test
    void verifyInvalidToken() {
        assert !jwtService.verify('')
        assert !jwtService.verify('token')
    }

    @Test
    void getTokenFromCookie() {
        def token = '123456token'
        Cookie cookie = new Cookie(AccessTokenService.TOKEN_FLAG_COOKIE, token)
        mockRequest.setCookies(cookie)
        assert accessTokenService.getToken(mockRequest).value == token

        mockRequest.setCookies(new Cookie('test1', 'test1'), new Cookie('test2', 'test2'))
        assert accessTokenService.getToken(mockRequest).value == null
    }

    @Test
    void getTokenFromRequestParam() {
        def token = 'a.b.1'
        mockRequest.setParameter(AccessTokenService.TOKEN_FLAG_URI, token)
        assert accessTokenService.getToken(mockRequest).value == token

        def token1 = ''
        mockRequest.setParameter(AccessTokenService.TOKEN_FLAG_URI, token1)
        assert accessTokenService.getToken(mockRequest).value == null
    }

    @Test(expected = ErrMsgException.class)
    void notSupportGenerate() {
        jwtService.generate()
    }

    @Test(expected = ErrMsgException.class)
    void notSupportSaveOrUpdate() {
        AccessTokenEntity accessToken = new AccessTokenEntity()
        jwtService.saveOrUpdate(accessToken)
    }

    @Test(expected = ErrMsgException.class)
    void notSupportDeleteByToken() {
        String token = "123"
        jwtService.deleteByToken(token)
    }
}
