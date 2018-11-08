package com.proper.enterprise.platform.configs.filter

import com.proper.enterprise.platform.test.AbstractIntegrationTest
import org.junit.Test
import org.springframework.http.HttpStatus

class AllowCrossOriginFilterTest extends AbstractIntegrationTest {

    @Test
    void checkResponseHeaders() {
        def connection = get('/test', HttpStatus.NOT_FOUND)
        def headers = connection.headerFields
        assert headers.containsKey('Access-Control-Allow-Credentials')
        assert headers.containsKey('Access-Control-Allow-Headers')
        assert headers.containsKey('Access-Control-Allow-Methods')
        assert headers.containsKey('Access-Control-Allow-Origin')
        assert headers.containsKey('Access-Control-Expose-Headers')
        assert headers.containsKey('Access-Control-Max-Age')

        options('/test', HttpStatus.OK)
    }

}
