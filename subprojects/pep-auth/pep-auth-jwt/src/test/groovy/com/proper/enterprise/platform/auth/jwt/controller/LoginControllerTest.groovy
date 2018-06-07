package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

class LoginControllerTest extends AbstractTest {
    def static final DEFAULT_USER = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")
    @Autowired
    JWTService jwtService

    @Autowired
    UserDao userDao

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/filter/adminUsers.sql")
    void login() {
        // all right
        mockLogin('admin', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        // wrong produce type
        mockLogin('admin', '123456', MediaType.APPLICATION_JSON, HttpStatus.NOT_ACCEPTABLE)
        // wrong password
        mockLogin('admin', '1234567', MediaType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED)
        // wrong account
        mockLogin('test', '1234567', MediaType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED)
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/filter/adminUsers.sql")
    void getCurrentUser() {
        def token = mockLogin('admin', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        mockRequest.addHeader("Authorization", token)
        Map<String, String> currentUserMap = JSONUtil.parse(get("/auth/login/user", HttpStatus.OK)
            .getResponse().getContentAsString(), HashMap)
        assert DEFAULT_USER == currentUserMap.get("userId")
        assert '超级管理员' == currentUserMap.get("name")
        assert 'avatar' == currentUserMap.get("avatar")
        assert '12' == currentUserMap.get("notifyCount")
    }

    private String mockLogin(String user, String pwd, MediaType produce, HttpStatus statusCode) {
        return post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode).getResponse().getContentAsString()
    }

}
