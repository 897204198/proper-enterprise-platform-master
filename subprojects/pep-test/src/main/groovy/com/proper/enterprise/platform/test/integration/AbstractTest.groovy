package com.proper.enterprise.platform.test.integration

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
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

    @Autowired
    protected MockHttpServletRequest mockRequest

    private def mockUser

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
        mockUser = null
    }

    protected void mockUser(String id='id', String username='uname', String password='pwd', boolean isSuper=false) {
        mockUser = [id: id, username: username, password: password, isSuper: isSuper]
        mockRequest.setAttribute('mockUser', mockUser)
    }

    protected MvcResult post(String url, String data, HttpStatus statusCode) {
        return post(url, MediaType.APPLICATION_JSON_UTF8, null, data, statusCode)
    }

    protected MvcResult post(String url, MediaType produces, String data, HttpStatus statusCode) {
        return post(url, MediaType.APPLICATION_JSON_UTF8, produces, data, statusCode)
    }

    protected MvcResult post(String url, MediaType consumes, MediaType produces, String data, HttpStatus statusCode) {
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

    protected MvcResult get(String url, HttpStatus statusCode) {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get(url)
        return perform(req, statusCode)
    }

    protected MvcResult put(String url, String data, HttpStatus statusCode) {
        return put(url, MediaType.APPLICATION_JSON_UTF8, null, data, statusCode)
    }

    protected MvcResult put(String url, MediaType produces, String data, HttpStatus statusCode) {
        return put(url, MediaType.APPLICATION_JSON_UTF8, produces, data, statusCode)
    }

    protected MvcResult put(String url, MediaType consumes, MediaType produces, String data, HttpStatus statusCode) {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.put(url)
        if (consumes != null) {
            req = req.contentType(consumes)
        }
        if (produces != null) {
            req = req.accept(produces)
        }
        req.content(data)
        return perform(req, statusCode)
    }

    protected MvcResult delete(String url, HttpStatus statusCode) {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.delete(url)
        return perform(req, statusCode)
    }

    protected MvcResult options(String url, HttpStatus statusCode) {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.options(url)
        return perform(req, statusCode)
    }

    private MvcResult perform(MockHttpServletRequestBuilder req, HttpStatus statusCode) {
        if (mockUser != null) {
            req.requestAttr('mockUser', mockUser)
        }
        return mockMvc
            .perform(req)
            .andDo(print())
            .andExpect(status().is(statusCode.value()))
            .andReturn()
    }

}
