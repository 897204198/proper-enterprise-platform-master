package com.proper.enterprise.platform.webapp.configs.controller

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus


class TestControllerTest extends AbstractTest {

    @Test
    public void checkDefaultCharsetUsedByStringHttpMessageConverter() {
        def str = '<p>中文乱码</p>'
        def result = getAndReturn("/test/get?str=$str", '', HttpStatus.OK)
        assert result == str
    }

}
