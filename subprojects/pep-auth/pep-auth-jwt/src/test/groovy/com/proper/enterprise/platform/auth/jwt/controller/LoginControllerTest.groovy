package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.core.model.CurrentModel
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.streamline.sdk.constants.StreamlineConstant
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import groovy.json.JsonSlurper
import org.apache.commons.codec.binary.Base64
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MvcResult

@Sql("/com/proper/enterprise/platform/auth/jwt/filter/adminUsers.sql")
class LoginControllerTest extends AbstractTest {

    def static final DEFAULT_USER = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")

    @Autowired
    private UserService userService

    @Test
    void loginAndLogout() {
        // all right
        mockLogin('admin', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        // wrong produce type
        mockLogin('admin', '123456', MediaType.APPLICATION_JSON, HttpStatus.NOT_ACCEPTABLE)
        // wrong password
        mockLogin('admin', '1234567', MediaType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED)
        // wrong account
        mockLogin('test', '1234567', MediaType.TEXT_PLAIN, HttpStatus.UNAUTHORIZED)
        Authentication.setCurrentUserId(DEFAULT_USER)
        post('/auth/logout', '', HttpStatus.CREATED)
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/jwt/sql/identity.sql")
    void getCurrentUser() {
        def token = mockLogin('testuser1', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        def payload = new JsonSlurper().parse(Base64.decodeBase64(token.split(/\./)[1]))
        assert payload.name == 'c'
        assert payload.hasRole == true

        mockRequest.addHeader("Authorization", token)
        Authentication.setCurrentUserId("user1")
        CurrentModel currentUserVO = JSONUtil.parse(get("/auth/current/user", HttpStatus.OK)
            .getResponse().getContentAsString(), CurrentModel.class)

        assert "user1" == currentUserVO.getId()
        assert 'c' == currentUserVO.getData().name
        assert 'avatar' == currentUserVO.getData().avatar
        assert 1 == currentUserVO.getData().roles.size()
        assert 1 == currentUserVO.getData().userGroups.size()
    }

    private String mockLogin(String user, String pwd, MediaType produce, HttpStatus statusCode) {
        return post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode).getResponse().getContentAsString()
    }

    private MvcResult mockLoginWithServiceKey(String user, String pwd, MediaType produce, HttpStatus statusCode, String serviceKey) {
        mockRequest.addHeader(StreamlineConstant.SERVICE_KEY, serviceKey)
        return post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode)
    }

    @Test
    void normalUserLogin() {
        def token1 = mockLogin('sam', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        def token2 = mockLogin('sam', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK)
        assert token1 != token2
    }

    @Test
    void normalUserLoginWithServiceKey() {
        def result = mockLoginWithServiceKey('sam', '123456', MediaType.TEXT_PLAIN, HttpStatus.OK, 'icmp')
        assert "icmp" == result.getResponse().getHeader(StreamlineConstant.SERVICE_KEY)
    }

}
