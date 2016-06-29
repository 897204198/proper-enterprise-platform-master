package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult


class UsersControllerTest extends AbstractTest {

    @Test
    public void createUser() {
        MvcResult result = post('/auth/users', '{username: "user1", password: "user1"}', HttpStatus.OK)
        UserEntity user = JSONUtil.parse(result.getResponse().getContentAsString(), UserEntity)
        get("/auth/users/${user.id}", HttpStatus.OK)
    }

    @Test
    public void retrieveUser() {
        get('/auth/users', [username: 'user1'], HttpStatus.NOT_FOUND)
        post('/auth/users', '{username: "user1", password: "user1"}', HttpStatus.OK)
        get('/auth/users', [username: 'user1'], HttpStatus.OK)
    }

    @Test
    public void updateUser() {
        MvcResult result = post('/auth/users', '{username: "user1", password: "user1"}', HttpStatus.OK)
        UserEntity user = JSONUtil.parse(result.getResponse().getContentAsString(), UserEntity)
        user.password = 'user1pwd'
        put("/auth/users/${user.id}", JSONUtil.toJSON(user), HttpStatus.OK)
        user = JSONUtil.parse(get("/auth/users/${user.id}", HttpStatus.OK).getResponse().getContentAsString(), UserEntity)
        assert user.password == 'user1pwd'
    }

    @Test
    public void deleteUser() {
        MvcResult result = post('/auth/users', '{username: "user1", password: "user1"}', HttpStatus.OK)
        UserEntity user = JSONUtil.parse(result.getResponse().getContentAsString(), UserEntity)
        delete("/auth/users/${user.id}", HttpStatus.OK)
    }

}
