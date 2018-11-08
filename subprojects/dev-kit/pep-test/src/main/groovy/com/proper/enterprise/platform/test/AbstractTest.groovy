package com.proper.enterprise.platform.test

import com.proper.enterprise.platform.test.utils.JSONUtil
import groovy.json.JsonSlurper
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.context.WebApplicationContext

import javax.servlet.Filter
import java.lang.reflect.Modifier

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * 基础测试类
 *
 * 包含测试常用的模拟对象、方法等
 */
@Transactional(transactionManager = "jpaTransactionManager")
@ContextConfiguration("/spring/applicationContext.xml")
@WebAppConfiguration
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SqlConfig(encoding = "UTF-8")
@Deprecated
abstract class AbstractTest {

    @Autowired
    protected WebApplicationContext wac

    @Autowired
    protected MockHttpServletRequest mockRequest

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor

    protected MockMvc mockMvc

    private def mockUser

    @Before
    void setup() {
        OpenEntityManagerInViewFilter openEntityManagerInViewFilter = new OpenEntityManagerInViewFilter()
        openEntityManagerInViewFilter.setServletContext(wac.servletContext)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(openEntityManagerInViewFilter, '/*').build()
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
        for (String header : mockRequest.getHeaderNames()) {
            req.header(header, mockRequest.getHeader(header))
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

    protected static void coverBean(Object bean) {
        bean.getClass().getDeclaredMethods().each { method ->
            println method
            if (shouldCover(method.modifiers)) {
                def params = []
                method.getParameterTypes().each { type ->
                    params << (type.isPrimitive() ? 0 : (type.isInterface() ? [] : type.newInstance()))
                }
                params.size() == 0 ? method.invoke(bean) :
                    (params.size() == 1 ? method.invoke(bean, params[0]) : method.invoke(bean, params))
            }
        }
    }

    private static boolean shouldCover(int modifiers) {
        !Modifier.isPrivate(modifiers) && !Modifier.isAbstract(modifiers)
    }

    /**
     * 为 RESTFul API 进行基本的增删改查测试
     *
     * @param uri       URI
     * @param entity    URI 代表的资源实体
     */
    protected void checkBaseCRUD(uri, entity) {
        checkBaseCreate(uri, entity)
        checkBaseRetrive(uri, entity)
        checkBaseUpdate(uri, entity)
        checkBaseDelete(uri, entity)
    }

    private def checkBaseCreate(uri, entity) {
        def e1 = postAndReturn(uri, entity)
        def e2 = getAndReturn(uri, e1, HttpStatus.OK)
        assert e1.properties.findAll(filterMillSecFields) == e2.properties.findAll(filterMillSecFields)
    }

    private Closure filterMillSecFields = {
        !it.key.endsWith('Time')
    }

    protected def postAndReturn(uri, entity) {
        def createdEntity = post(uri.toString(), JSONUtil.toJSON(entity), HttpStatus.CREATED).response.contentAsString
        JSONUtil.parse(createdEntity, entity.getClass())
    }

    /**
     * @deprecated Try to use resOfGet instead
     */
    @Deprecated
    protected def getAndReturn(uri, entity, status) {
        def str = get(uri + (entity.hasProperty('id') && entity.id > '' ? "/${entity.id}" : ''), status)
                    .getResponse().getContentAsString()
        def clz = entity.class
        if (StringUtils.isEmpty(str) || clz.equals(String.class)) {
            return str
        }
        return str.startsWith('[') ? JSONUtil.parse(str, clz[].class) : (str.startsWith('{') ? JSONUtil.parse(str, clz) : str)
    }

    protected def resOfGet(uri, status) {
        def str = get(uri, status).response.contentAsString
        try {
            str = new JsonSlurper().parseText(str)
        } catch (ex) { }
        str
    }

    private def checkBaseRetrive(uri, entity) {
        def notFoundEntity = entity.class.newInstance()
        notFoundEntity.id = 'NOT_FOUND_ENTITY'
        assert getAndReturn(uri, notFoundEntity, HttpStatus.OK) == ''

        def e1 = postAndReturn(uri, entity)
        getAndReturn(uri, e1, HttpStatus.OK)
    }

    private def checkBaseUpdate(uri, entity) {
        def e1 = postAndReturn(uri, entity)
        def property = entity.class.declaredFields.find { it.type == String.class }.name
        def newVal = 'new value'
        e1[property] = newVal
        assert putAndReturn(uri, e1, HttpStatus.OK)[property] == newVal

        deleteAndReturn(uri, e1.id, HttpStatus.NO_CONTENT)
        assert putAndReturn(uri, e1, HttpStatus.OK) == ''
    }

    protected def putAndReturn(uri, entity, status) {
        def str = put("$uri/${entity.id}", JSONUtil.toJSON(entity), status).getResponse().getContentAsString()
        return str > '' ? JSONUtil.parse(str, entity.class) : str
    }

    protected def deleteAndReturn(uri, id, status) {
        delete("$uri/$id", status)
    }

    private def checkBaseDelete(uri, entity) {
        def e1 = postAndReturn(uri, entity)
        getAndReturn(uri, e1, HttpStatus.OK)
        deleteAndReturn(uri, e1.id, HttpStatus.NO_CONTENT)
        deleteAndReturn(uri, e1.id, HttpStatus.NOT_FOUND)

        assert getAndReturn(uri, e1, HttpStatus.OK) == ''
    }

    protected def waitExecutorDone() {
        while (threadPoolTaskExecutor.activeCount > 0) {
            println "sleep 5 milliseconds to wait, current active count is ${threadPoolTaskExecutor.activeCount}"
            sleep(5)
        }
    }

    /**
     * 按照名称覆盖单例 bean
     *
     * @param beanName          bean 名称
     * @param singletonObject   单例对象
     */
    protected void overrideSingleton(String beanName, Object singletonObject) {
        DefaultListableBeanFactory bf = (DefaultListableBeanFactory) wac.getAutowireCapableBeanFactory()
        bf.destroySingleton(beanName)
        bf.registerSingleton(beanName, singletonObject)
    }

}
