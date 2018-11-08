package com.proper.enterprise.platform.dev.tools.controller

import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MvcResult

class AESControllerTest extends AbstractSpringTest {

    MediaType consumes = MediaType.TEXT_PLAIN
    MediaType produces = MediaType.TEXT_PLAIN

    @Test
    void encode() throws Exception {
        MvcResult encryption = post("/admin/dev/aes/encrypt", consumes, produces, '123', HttpStatus.CREATED)
        String resultContent = encryption.getResponse().getContentAsString()
        assert resultContent.equals('vmNsvKKJT4XO3HE+pQPirg==')
    }

    @Test
    void decode() throws Exception {
        MvcResult decryption = post("/admin/dev/aes/decrypt", consumes, produces, 'vmNsvKKJT4XO3HE+pQPirg==', HttpStatus.CREATED)
        String resultContent = decryption.getResponse().getContentAsString()
        assert resultContent.equals('123')
    }

}
