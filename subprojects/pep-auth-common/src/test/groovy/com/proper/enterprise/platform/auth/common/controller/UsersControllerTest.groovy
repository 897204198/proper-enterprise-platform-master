package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MvcResult

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
        if (str > '') {
            return JSONUtil.parse(
                get("/auth/users$suffix", status)
                    .getResponse()
                    .getContentAsString(),
                UserEntity
            )
        }
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
