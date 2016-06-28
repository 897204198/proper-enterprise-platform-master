package com.proper.enterprise.platform.test.integration

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("/spring/applicationContext.xml")
@Transactional
@ActiveProfiles("test")
public abstract class AbstractTest {

    @Autowired
    private WebApplicationContext wac

    protected MockMvc mockMvc

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }

    protected MvcResult post(String url, String data, int statusCode) {
        return post(url, MediaType.APPLICATION_JSON_UTF8, null, data, statusCode)
    }

    protected MvcResult post(String url, MediaType produces, String data, int statusCode) {
        return post(url, MediaType.APPLICATION_JSON_UTF8, produces, data, statusCode)
    }

    protected MvcResult post(String url, MediaType consumes, MediaType produces, String data, int statusCode) {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post(url)
        if (consumes != null) {
            req = req.contentType(consumes)
        }
        if (produces != null) {
            req = req.accept(produces)
        }
        req.content(data)
        return perform(req, statusCode)
    }

    private MvcResult perform(MockHttpServletRequestBuilder req, int statusCode) {
        return mockMvc
            .perform(req)
            .andDo(print())
            .andExpect(status().is(statusCode))
            .andReturn()
    }

}
