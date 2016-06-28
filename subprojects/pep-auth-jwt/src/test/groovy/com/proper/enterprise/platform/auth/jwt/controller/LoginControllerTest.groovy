package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.auth.jwt.service.JWTService
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

class LoginControllerTest extends AbstractTest {

    @Autowired
    JWTService jwtService

    @Test
    @Sql
    public void login() {
        mockLogin('admin', '123456', HttpStatus.OK)
        mockLogin('admin', '1234567', HttpStatus.UNAUTHORIZED)
        mockLogin('test', '1234567', HttpStatus.UNAUTHORIZED)
    }

    private void mockLogin(String user, String pwd, HttpStatus statusCode) {
        post('/auth/login', MediaType.TEXT_PLAIN, """{"username":"$user","pwd":"$pwd"}""", statusCode)
    }

}
