package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.auth.jwt.service.JWTService
import com.proper.enterprise.platform.test.AbstractTest
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
        // all right
        mockLogin('admin', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        // wrong produce type
        mockLogin('admin', '123456', MediaType.APPLICATION_JSON, HttpStatus.NOT_ACCEPTABLE)
        // wrong password
        mockLogin('admin', '1234567', MediaType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED)
        // wrong account
        mockLogin('test', '1234567', MediaType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED)
    }

    private void mockLogin(String user, String pwd, MediaType produce, HttpStatus statusCode) {
        post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode)
    }

}
