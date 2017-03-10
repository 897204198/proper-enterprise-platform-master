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

    @Test
    public void testUserCache() {
        def u1 = new UserEntity('u1','p1')
        userService.save(u1)
        println 'After @CachePut on save method, should not query from DB'
        assert u1.id != null
        3.times { idx ->
            assert userService.get(u1.id) == u1
        }
        u1.setEmail('a@b')
        userService.save(u1)
        3.times { idx ->
            assert userService.get(u1.id).email == 'a@b'
        }

        def u2 = new UserEntity('u2','p2')
        def u3 = new UserEntity('u3','p3')
        userService.save(u2, u3)
        println 'Batch save should evict all caches, trigger query after this'
        3.times { idx ->
            assert userService.get(u1.id) == u1
            println "after batch save $idx query"
        }

        userService.delete(u1.id)
        println 'Also need direct query after delete'
        assert userService.get(u1.id) == null
    }

}
