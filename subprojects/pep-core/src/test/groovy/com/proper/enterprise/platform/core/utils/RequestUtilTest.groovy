package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest


class RequestUtilTest extends AbstractTest {

    @Autowired
    MockHttpServletRequest request

    @Test
    public void test() {
        def uri = '/foo/bar'
        request.setRequestURI uri

        assert RequestUtil.getCurrentRequest().getRequestURI() == uri
    }

}
