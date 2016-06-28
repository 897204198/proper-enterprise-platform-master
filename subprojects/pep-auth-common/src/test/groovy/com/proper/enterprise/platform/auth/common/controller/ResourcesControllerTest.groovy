package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus

class ResourcesControllerTest extends AbstractTest {

    @Test
    public void test() {
        post('/auth/resources', '{"url":"/foo/bar", "method": "PUT"}', HttpStatus.OK)
        post('/auth/resources', '{"url":"/foo/bar", "method": "PUT"}', HttpStatus.OK)
    }

}
