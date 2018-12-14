package com.proper.enterprise.platform.auth.aspect

import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.service.APISecret
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

class JWTAuthAspectTest extends AbstractTest {

    @Autowired
    private APISecret secret

    @Autowired
    private JWTService jwtService

    @Autowired
    private UserService userService

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/sql/identity.sql")
    public void userUpdate() {
        mockLogin('testuser1', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        def oldSec = secret.getAPISecret("user1")
        User user = userService.get("user1")
        user.setEnable(false)
        userService.update(user)
        assert oldSec != secret.getAPISecret("user1")
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/sql/identity.sql")
    public void userUpdateEnable() {
        mockLogin('testuser3', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        mockLogin('testuser2', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        def oldSec3 = secret.getAPISecret("user3")
        def oldSec2 = secret.getAPISecret("user2")
        List<String> ids = new ArrayList<>()
        ids.add("user2")
        ids.add("user3")
        userService.updateEnable(ids, false)
        assert oldSec3 != secret.getAPISecret("user3")
        assert oldSec2 != secret.getAPISecret("user2")
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/sql/identity.sql")
    public void userDelete() {
        mockLogin('testuser3', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        mockLogin('testuser2', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        def oldSec3 = secret.getAPISecret("user3")
        def oldSec2 = secret.getAPISecret("user2")
        userService.deleteByIds("user3,user2")
        assert oldSec3 != secret.getAPISecret("user3")
        assert oldSec2 != secret.getAPISecret("user2")
    }


    private String mockLogin(String user, String pwd, MediaType produce, HttpStatus statusCode) {
        return post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode).getResponse().getContentAsString()
    }
}
