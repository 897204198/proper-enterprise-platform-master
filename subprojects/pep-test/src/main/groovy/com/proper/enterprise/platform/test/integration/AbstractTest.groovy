package com.proper.enterprise.platform.test.integration
import com.proper.enterprise.platform.test.integration.utils.JSONUtil
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

import javax.servlet.Filter

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
/**
 * 基础测试类
 *
 * 包含测试常用的模拟对象、方法等
 */
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

    /**
     * 在模拟请求中，模拟出一个用户
     * @param id        用户 id
     * @param username  用户名
     * @param password  密码
     * @param isSuper   是否超级用户
     */
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

    /**
     * 模拟请求，并响应状态码
     * 可用于模拟 RESTFul 请求
     *
     * @param req           模拟请求构造器
     * @param statusCode    期望的响应状态
     * @return
     */
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

    /**
     * 为了单元测试覆盖到 filter 的 init 和 destory 方法，
     * 需在单元测试中显示调用一下这个方法，暂未找到更好的方法
     *
     * @param filter 要测试的 filter
     */
    protected static void coverFilter(Filter filter) {
        filter.init(null)
        filter.destroy()
    }

    // TODO
    def checkBaseCRUD(uri, entity) {
        checkBaseCreate(uri, entity)
        checkBaseRetrive(uri, entity)
    }

    private def checkBaseCreate(uri, entity) {
        def e1 = postAndReturn(uri, entity)
        def e2 = getAndReturn(uri, e1)
        assert e1.properties == e2.properties
    }

    private def postAndReturn(uri, entity) {
        def createdEntity = post(uri.toString(), JSONUtil.toJSON(entity), HttpStatus.CREATED).response.contentAsString
        JSONUtil.parse(createdEntity, entity.class)
    }

    private def getAndReturn(uri, entity, status=HttpStatus.OK) {
        def str = get(uri + (entity.id > '' ? "/${entity.id}" : ''), status).getResponse().getContentAsString()
        def clz = entity.class
        if (str > '') {
            return str.startsWith('[') ? JSONUtil.parse(str, clz[].class) : JSONUtil.parse(str, clz)
        }
    }

    private def checkBaseRetrive(uri, entity) {
        def notFoundEntity = entity.class.newInstance()
        notFoundEntity.id = 'NOT_FOUND_ENTITY'
        getAndReturn(uri, notFoundEntity, HttpStatus.NOT_FOUND)

        def e1 = postAndReturn(uri, entity)
        getAndReturn(uri, e1)
    }

    def checkBaseUpdate(uri, entity) {
        // TODO
    }

    def checkBaseDelete(uri, entity) {
        // TODO
    }

}
