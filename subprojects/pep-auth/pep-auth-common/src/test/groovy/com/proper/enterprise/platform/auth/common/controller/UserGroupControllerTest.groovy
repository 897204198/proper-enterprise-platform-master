package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.model.UserGroup
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserGroupEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UserGroupControllerTest extends AbstractTest {

    private static final String URI = '/auth/user-groups'
    private static UserGroup group = new UserGroupEntity('oopstrom')

    @Autowired
    private UserService userService

    @Test
    void checkCRUD() {
        checkBaseCRUD(URI, group)
    }

    @Test
    @Sql("/com/proper/enterprise/platform/auth/common/users.sql")
    void addUsersToGroupAndThenRemove() {
        def u1 = userService.getByUsername('t1')
        def u2 = userService.getByUsername('t2')
        def u3 = userService.getByUsername('t3')
        UserGroupEntity oopstorm = postAndReturn(URI, group)
        oopstorm.add(u1, u2, u3)
        oopstorm = putAndReturn(URI, oopstorm, HttpStatus.OK)
        assert oopstorm.users.containsAll([u1, u2, u3])

        oopstorm.remove(u2)
        assert putAndReturn(URI, oopstorm, HttpStatus.OK).users.size == 2
    }

}
