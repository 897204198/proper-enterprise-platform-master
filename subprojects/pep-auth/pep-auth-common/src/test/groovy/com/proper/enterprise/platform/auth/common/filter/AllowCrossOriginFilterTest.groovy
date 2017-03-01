package com.proper.enterprise.platform.auth.common.filter

import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import javax.servlet.http.HttpServletResponse

class AllowCrossOriginFilterTest extends AbstractTest {

    @Autowired
    private WebApplicationContext wac

    @Test
    public void allowCrossOriginFilterShouldSetResponseHeaders() {
        def filter = new AllowCrossOriginFilter()
        coverFilter(filter)

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(filter, '/*').build()
        checkResponse get('/test', HttpStatus.NOT_FOUND)
        checkResponse options('/test', HttpStatus.OK)
    }

    def checkResponse(MvcResult result) {
        HttpServletResponse response = result.getResponse()
        assert response.getHeader('Access-Control-Allow-Origin') == ConfCenter.get("auth.access_control.allow_origin")
        assert response.getHeader('Access-Control-Allow-Methods') == ConfCenter.get("auth.access_control.allow_methods")
        assert response.getHeader('Access-Control-Allow-Headers') == ConfCenter.get("auth.access_control.allow_headers")
        assert response.getHeader('Access-Control-Max-Age') == ConfCenter.get("auth.access_control.max_age")
    }

}
