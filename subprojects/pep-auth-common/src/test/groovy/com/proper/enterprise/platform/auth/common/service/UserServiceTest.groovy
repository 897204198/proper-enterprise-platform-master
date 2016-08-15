package com.proper.enterprise.platform.auth.common.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


class UserServiceTest extends AbstractTest {

    @Autowired
    UserService userService

    @Test
    public void setEmail() {
        def user = new UserEntity()
        user.setUsername('uname')
        user.setPassword('pwd')
        user.setEmail('a@b.com')
        assert userService.save(user).getEmail() == 'a@b.com'
    }

}
