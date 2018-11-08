package com.proper.enterprise.platform.auth.service.impl

import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImplTest extends AbstractJPATest {

    @Autowired
    UserDao userDao

    @Autowired
    JWTService jwtService

    @Autowired
    UserService userService

    def static final DEFAULT_USER =  PEPPropertiesLoader.load(CoreProperties.class).getDefaultOperatorId()

    @Before
    void initData() {
        tearDown()
    }

    @After
    void cleanAll() {
        tearDown()
    }

    void tearDown() {
        userDao.deleteAll()
    }

    @Test
    void getUserFromToken() {
        User userNodeEntity = userDao.getNewUser()
        userNodeEntity.setUsername('unm')
        userNodeEntity.setPassword('e10adc3949ba59abbe56e057f20f883e')
        userDao.save(userNodeEntity)

        def token = jwtService.generateToken(new JWTHeader(userNodeEntity.getId(), 'unm'), new JWTPayloadImpl())
        mockRequest.addHeader('Authorization', token)
        Authentication.setCurrentUserId(userNodeEntity.getId())
        User user1 = userDao.getNewUser()
        user1.setUsername('a')
        user1.setPassword('b')
        def user = userDao.save(user1)
        assert user.getCreateUserId() == userService.getCurrentUser().getId()
    }

    @Test
    void fallbackDefaultUserUsingFakeToken() {
        mockRequest.addHeader('Authorization', 'a.b.c')
        Authentication.setCurrentUserId(DEFAULT_USER)
        User user1 = userDao.getNewUser()
        user1.setUsername('a')
        user1.setPassword('b')
        def user = userDao.save(user1)
        assert user.getCreateUserId() == DEFAULT_USER
    }

}
