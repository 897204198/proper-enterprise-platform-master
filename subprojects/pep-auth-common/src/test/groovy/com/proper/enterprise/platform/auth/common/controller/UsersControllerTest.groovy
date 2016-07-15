package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql

class UsersControllerTest extends AbstractTest {

    private static final String URI = '/auth/users'
    private static UserEntity userEntity = new UserEntity('user1', 'pwd1')

    @Test
    public void checkCRUD() {
        checkBaseCRUD(URI, userEntity)
    }

    @Autowired
    RoleService roleService

    @Sql
    @Test
    public void addRolesToUserAndThenRemove() {
        UserEntity user = postAndReturn(URI, userEntity)
        def roles = roleService.getByName('testrole')
        assert !roles.isEmpty() && roles.size()==2
        user.add(roles[0])
        user.add(roles[1])
        user = putAndReturn(URI, user, HttpStatus.OK)
        assert user.roles.containsAll(roles)

        def role = roleService.get('role1')
        user.remove(role)
        assert putAndReturn(URI, user, HttpStatus.OK).roles.first().id == 'role2'
    }

}
