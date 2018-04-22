package com.proper.enterprise.platform.auth.service.impl

import com.proper.enterprise.platform.api.auth.dao.UserDao
import com.proper.enterprise.platform.api.auth.model.User
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader
import com.proper.enterprise.platform.auth.jwt.model.impl.JWTPayloadImpl
import com.proper.enterprise.platform.auth.service.JWTService
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImplTest extends AbstractTest {

    @Autowired
    UserDao userDao

    @Autowired
    JWTService jwtService

    @Autowired
    UserService userService

    def static final DEFAULT_USER = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")


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

        User user1 = userDao.getNewUser()
        user1.setUsername('a')
        user1.setPassword('b')
        def user = userDao.save(user1)
        assert user.getCreateUserId() == userService.getCurrentUser().getId()
    }

    @Test
    @NoTx
    void fallbackDefaultUserWithoutToken() {
        User user1 = userDao.getNewUser()
        user1.setUsername('a')
        user1.setPassword('b')
        def user = userDao.save(user1)
        assert user.getCreateUserId() == DEFAULT_USER
    }

    @Test
    void fallbackDefaultUserUsingFakeToken() {
        mockRequest.addHeader('Authorization', 'a.b.c')
        User user1 = userDao.getNewUser()
        user1.setUsername('a')
        user1.setPassword('b')
        def user = userDao.save(user1)
        assert user.getCreateUserId() == DEFAULT_USER
    }

}
