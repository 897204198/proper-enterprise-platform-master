package com.proper.enterprise.platform.integration.auth.jwt

import com.proper.enterprise.platform.auth.jwt.APISecret
import com.proper.enterprise.platform.auth.jwt.JWTService
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class JWTServiceIntegTest extends AbstractIntegTest {

    @Autowired
    private JWTService jwtService

    @Autowired
    private APISecret secret

    @Test
    public void testGenerateAndVerifyToken() {
        def header = new JWTHeader()
        header.setId('1')
        header.setName('test')
        header.setExpire(System.currentTimeMillis() + 1000);
        def payload = new JWTPayloadImpl()
        payload.setRoles('a,b,c')
        def token = jwtService.generateToken(header, payload)

        assert jwtService.verify(token)
    }

    @Test
    public void testMultiTokens() {
        def header1 = new JWTHeader('1', 't1', System.currentTimeMillis()+2000)
        def payload1 = new JWTPayloadImpl('t1emp')
        def secret1 = secret.getAPISecret(header1.getId())
        def token1 = jwtService.generateToken(header1, payload1)

        def header2 = new JWTHeader('2', 't2', System.currentTimeMillis()+2000)
        def payload2 = new JWTPayloadImpl('t2emp')
        def secret2 = secret.getAPISecret(header2.getId())
        def token2 = jwtService.generateToken(header2, payload2)

        assert secret1 == secret.getAPISecret(header1.getId())
        assert secret2 == secret.getAPISecret(header2.getId())
        assert secret.getAPISecret(header1.getId()) != secret.getAPISecret(header2.getId())

        assert jwtService.verify(token2)
        assert jwtService.verify(token1)
    }

}
