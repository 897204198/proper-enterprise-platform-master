package com.proper.enterprise.platform.auth.common.controller
import com.proper.enterprise.platform.api.auth.service.RoleService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

class UsersControllerTest extends AbstractTest {

    @Test
    public void createUser() {
        def u1 = doPost()
        def u2 = doGet("/${u1.id}", HttpStatus.OK)
        assert u1.username == u2.username
        assert u1.password == u2.password
    }

    private UserEntity doPost() {
        JSONUtil.parse(
            post('/auth/users',
                 MediaType.APPLICATION_JSON_UTF8,
                 '{"username": "user1", "password": "user1"}',
                 HttpStatus.CREATED)
                .getResponse()
                .getContentAsString(),
            UserEntity
        )
    }

    private UserEntity doGet(String suffix, HttpStatus status) {
        def str = get("/auth/users$suffix", status).getResponse().getContentAsString()
        return str > '' ? JSONUtil.parse(str, UserEntity) : null
    }

    private UserEntity doPut(String id, UserEntity user, HttpStatus status) {
        def str = put("/auth/users/$id", JSONUtil.toJSON(user), status).getResponse().getContentAsString()
        return str > '' ? JSONUtil.parse(str, UserEntity) : null
    }

    private void doDelete(String id, HttpStatus status) {
        delete("/auth/users/$id", status)
    }

    @Test
    public void retrieveUser() {
        doGet('?name=user1', HttpStatus.NOT_FOUND)
        doPost()
        doGet('?name=user1', HttpStatus.OK)
    }

    @Test
    public void updateUser() {
        UserEntity user = doPost()
        user.password = 'user1pwd'
        assert doPut(user.id, user, HttpStatus.OK).password == 'user1pwd'

        doDelete(user.id, HttpStatus.NO_CONTENT)
        doPut(user.id, user, HttpStatus.NOT_FOUND)
    }

    @Test
    public void deleteUser() {
        UserEntity user = doPost()
        doGet("/${user.id}", HttpStatus.OK)
        doDelete(user.id, HttpStatus.NO_CONTENT)
        doDelete(user.id, HttpStatus.NOT_FOUND)

        doGet("/${user.id}", HttpStatus.NOT_FOUND)
    }

    @Autowired
    RoleService roleService

    @Sql
    @Test
    public void addRolesToUserAndThenRemove() {
        UserEntity user = doPost()
        def roles = roleService.getByName('testrole')
        assert !roles.isEmpty() && roles.size()==2
        user.addRole(roles[0])
        user.addRole(roles[1])
        user = doPut(user.id, user, HttpStatus.OK)
        assert user.roles.containsAll(roles)

        def role = roleService.get('role1')
        user.removeRole(role)
        assert doPut(user.id, user, HttpStatus.OK).roles.first().id == 'role2'
    }

}
