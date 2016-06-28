package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.auth.jwt.service.JWTService
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class LoginControllerTest extends AbstractTest {

    @Autowired
    JWTService jwtService

    @Test
    @Sql
    public void login() {
        mockLogin('admin', '123456', HttpStatus.OK.value())
        mockLogin('admin', '1234567', HttpStatus.UNAUTHORIZED.value())
        mockLogin('test', '1234567', HttpStatus.UNAUTHORIZED.value())
    }

    private void mockLogin(String user, String pwd, int statusCode) {
        mockMvc
            .perform(
                post('/auth/login')
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.TEXT_PLAIN)
                    .content("""{"username":"$user","pwd":"$pwd"}""")
            )
            .andDo(print())
            .andExpect(status().is(statusCode))
    }

}
