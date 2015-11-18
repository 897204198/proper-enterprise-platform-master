package com.proper.enterprise.platform.integration.auth.jwt

import com.proper.enterprise.platform.auth.jwt.JWTService
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class JWTServiceIntegTest extends AbstractIntegTest {

    @Autowired
    private JWTService jwtService

    @Test
    public void testGenerateToken() {
        def header = new JWTHeader()
        header.setUid('1')
        header.setUname('test')
        header.setExpire(System.currentTimeMillis() + 1000);
        def payload = new JWTPayloadImpl()
        payload.setRoles('a,b,c')
        def token = jwtService.generateToken(header, payload)
        def apiSecret = jwtService.getAPISecret(header.uid)

        println token
        println apiSecret
    }

}