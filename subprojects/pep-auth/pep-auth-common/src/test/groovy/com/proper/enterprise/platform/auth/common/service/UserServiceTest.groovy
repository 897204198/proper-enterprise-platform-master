package com.proper.enterprise.platform.auth.common.service

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

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

    @Test
    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    public void checkSuperuser() {
        assert userService.getByUsername('t1').isSuperuser()
        assert !userService.getByUsername('t2').isSuperuser()
        assert !userService.getByUsername('t3').isSuperuser()
    }

}
