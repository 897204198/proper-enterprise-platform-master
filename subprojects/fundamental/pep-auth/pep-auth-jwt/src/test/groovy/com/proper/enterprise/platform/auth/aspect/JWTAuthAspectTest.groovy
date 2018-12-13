package com.proper.enterprise.platform.auth.aspect

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.service.APISecret
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

class JWTAuthAspectTest extends AbstractJPATest {

    @Autowired
    private APISecret secret

    @Autowired
    private JWTService jwtService

    @Autowired
    private UserService userService

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/sql/identity.sql")
    public void userDetailEnable() {
//        assert null == secret.getAPISecret("user1")
        def token = mockLogin('testuser1', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        assert secret.getAPISecret("user1") != null
        User user = userService.get("user1")
        user.setEnable(false)
        userService.update(user)
        jwtService.getByUserId()
        jwtService.clearToken(jwtService.getHeader(token))
        secret.clearAPISecret("user1")
        assert null == secret.getAPISecret("user1")
    }

    private String mockLogin(String user, String pwd, MediaType produce, HttpStatus statusCode) {
        return post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode).getResponse().getContentAsString()
    }
}
