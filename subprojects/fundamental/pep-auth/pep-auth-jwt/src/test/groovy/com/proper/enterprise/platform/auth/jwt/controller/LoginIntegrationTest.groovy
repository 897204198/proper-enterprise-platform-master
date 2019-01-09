package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.core.model.CurrentModel
import com.proper.enterprise.platform.test.AbstractIntegrationTest
import groovy.json.JsonSlurper
import org.apache.commons.codec.binary.Base64
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

@Sql("/com/proper/enterprise/platform/auth/jwt/filter/adminUsers.sql")
class LoginIntegrationTest extends AbstractIntegrationTest {

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/sql/identity.sql")
    void getCurrentUser() {
        def token = mockLogin('testuser1', '123456', HttpStatus.OK)
        def payload = new JsonSlurper().parse(Base64.decodeBase64(token.split(/\./)[1]))
        assert payload.name == 'c'
        assert payload.hasRole == true

        CurrentModel currentUserVO = resOfGet("/auth/current/user?access_token=" + token, HttpStatus.OK)

        assert "user1" == currentUserVO.getId()
        assert 'c' == currentUserVO.getData().name
        assert 'avatar' == currentUserVO.getData().avatar
        assert 1 == currentUserVO.getData().roles.size()
        assert 1 == currentUserVO.getData().userGroups.size()
    }

    private String mockLogin(String user, String pwd, HttpStatus statusCode) {
        return resOfPost('/auth/login', """{"username":"$user","pwd":"$pwd"}""", statusCode)
    }
}
