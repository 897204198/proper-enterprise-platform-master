package com.proper.enterprise.platform.auth.jwt.controller

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.utils.JSONUtil
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

@Sql
class JWTTokenControllerTest extends AbstractTest {

    def token = 'eyJpZCI6Ijc3YmMzMDJlLWQzNzAtNDk0NS05ZDkxLTc5ZjIxYzE3Yzg2ZSIsIm5hbWUiOiIxNTY0MDU2Nzc4MCJ9'
    def header = '{"id":"77bc302e-d370-4945-9d91-79f21c17c86e","name":"15640567780"}'

    @Test
    void testEncode() throws Exception {
        encode(JSONUtil.toJSON([name: '15640567780']), token)
        encode(JSONUtil.toJSON([id: '1']), 'eyJpZCI6IjE')
    }

    def encode(String input, String output) {
        def res = post("/admin/dev/jwt/encode/header", input ,HttpStatus.CREATED).getResponse().getContentAsString()
        assert res == output
    }

    @Test
    void testDecode() throws Exception {
        decode(token, header)
        decode('eyJpZCI6IjE', '{"id":"1')
        decode('eyJpZCI6IlBFUF9TWVMiLCJuYW1lIjoiYWRtaW4ifQ.eyJuYW1lIjoi6LaF57qn566h55CG5ZGYIiwiaGFzUm9sZSI6dHJ1ZX0', '{"id":"PEP_SYS","name":"admin"}.{"name":"超级管理员","hasRole":true}')
    }

    def decode(String input, String output) {
        def res = post("/admin/dev/jwt/decode", MediaType.TEXT_PLAIN, MediaType.TEXT_PLAIN, input, HttpStatus.CREATED).getResponse()
                .getContentAsString()
        assert res == output
    }

}
