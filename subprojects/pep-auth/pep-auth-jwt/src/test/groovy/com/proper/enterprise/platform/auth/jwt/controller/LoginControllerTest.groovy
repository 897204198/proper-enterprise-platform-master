package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class LoginControllerTest extends AbstractTest {
    def static final DEFAULT_USER = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")
    @Autowired
    JWTService jwtService

    @Autowired
    UserDao userDao

    @Before
    void initData() {
        tearDown()
        User user = userDao.getNewUser()
        user.setId('dc65766c-0176-4a1e-ad0e-dd06ba645c7l')
        user.setUsername('admin')
        user.setPassword('e10adc3949ba59abbe56e057f20f883e')
        userDao.save(user)
    }

    @After
    void cleanAll() {
        tearDown()
    }

    void tearDown() {
        userDao.deleteAll()
    }

    @Test
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
    void getCurrentUser() {
        mockUser(DEFAULT_USER,'isTest', '123456')
        Map<String, String> currentUserMap = JSONUtil.parse(get("/auth/login/user", HttpStatus.OK)
            .getResponse().getContentAsString(),HashMap)
        assert DEFAULT_USER==currentUserMap.get("userId")
        assert ' '==currentUserMap.get("name")
        assert 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png'==currentUserMap.get("avatar")
        assert '12'==currentUserMap.get("notifyCount")
    }

    private void mockLogin(String user, String pwd, MediaType produce, HttpStatus statusCode) {
        post('/auth/login', produce, """{"username":"$user","pwd":"$pwd"}""", statusCode)
    }

}
