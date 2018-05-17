package com.proper.enterprise.platform.core.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.controller.mock.MockEntity
import com.proper.enterprise.platform.core.controller.mock.MockEntityR
import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MvcResult

class BaseControllerTest extends AbstractTest {

    def c = new BaseController() {}
    def entity = new MockEntityR('abc')

    @Test
    void testPOSTResponse() {
        def result = c.responseOfPost(entity)
        assert result.getStatusCode() == HttpStatus.CREATED
        assert result.getBody() == entity
    }

    @Test
    void testGETResponse() {
        // Get one entity
        def result = c.responseOfGet(entity)
        assert result.getStatusCode() == HttpStatus.OK
        assert result.getBody() == entity

        // Get an entities collection
        result = c.responseOfGet([entity, [c: 3, d: 4]])
        assert result.getStatusCode() == HttpStatus.OK
        assert result.getBody().get(0) == entity

        // Get nothing
        result = c.responseOfGet(null)
        assert result.getStatusCode() == HttpStatus.OK
        assert result.getBody() == null

        result = c.responseOfGet([], new HttpHeaders(['Content-Type': [MediaType.TEXT_PLAIN_VALUE]], true))
        assert result.getStatusCode() == HttpStatus.OK
        assert result.getBody() == []
        assert result.getHeaders().getContentType() == MediaType.TEXT_PLAIN

        def r = get('/core/test/emptylist', HttpStatus.OK)
        assert r.getResponse().getContentType() == MediaType.APPLICATION_JSON_UTF8_VALUE
        assert r.getResponse().getContentAsString() == '[]'
    }

    @Test
    void testPUTResponse() {
        // Same internal method as responseOfGet
        def result = c.responseOfPut(entity)
        assert result.getStatusCode() == HttpStatus.OK
        assert result.getBody() == entity
    }

    @Test
    void testDELETEResponse() {
        def result = c.responseOfDelete(true)
        assert result.getStatusCode() == HttpStatus.NO_CONTENT
        assert result.getBody() == null

        result = c.responseOfDelete(false)
        assert result.getStatusCode() == HttpStatus.NOT_FOUND
        assert result.getBody() == null
    }

    @Test
    void handleTrouble() {
        def textPlainUtf8 = MediaType.TEXT_PLAIN_VALUE + ';charset=UTF-8'

        def r = get('/core/test/trouble/1?div=1', HttpStatus.OK)
        assert r.getResponse().getContentType() == MediaType.APPLICATION_JSON_UTF8_VALUE

        def r1 = get('/core/test/trouble/1?div=0', HttpStatus.INTERNAL_SERVER_ERROR)
        assert 'Division by zero' == r1.getResponse().getContentAsString()
        assert r1.getResponse().getContentType() == textPlainUtf8
        def r2 = get('/core/test/trouble/1?div=abc', HttpStatus.INTERNAL_SERVER_ERROR)
        assert 'For input string: "abc"' == r2.getResponse().getContentAsString()

        def r3 = get('/core/test/trouble/2', HttpStatus.INTERNAL_SERVER_ERROR)
        assert '异常啦' == r3.getResponse().getContentAsString()
        assert r3.getResponse().getContentType() == textPlainUtf8
        assert r3.getResponse().getHeader(PEPConstants.RESPONSE_HEADER_ERROR_TYPE) == PEPConstants.RESPONSE_SYSTEM_ERROR
        get('/core/test/trouble/3', HttpStatus.NOT_FOUND)

        MvcResult bussinessErrResult = get('/core/test/trouble/4', HttpStatus.INTERNAL_SERVER_ERROR)
        assert bussinessErrResult.getResponse().getContentAsString() == 'empty stack'
        assert bussinessErrResult.getResponse().getHeader(PEPConstants.RESPONSE_HEADER_ERROR_TYPE) == PEPConstants.RESPONSE_BUSINESS_ERROR
    }

    @Test
    void ignorePropertyInEntity() {
        def r = get('/core/test/json/entity', HttpStatus.OK).getResponse().getContentAsString()
        def m = JSONUtil.parse(r, Map.class)
        assert m.size() == 4
        assert m.get("enable")
        assert m.containsKey('id')
        assert m.containsKey('lastModifyTime')
        assert m.containsKey('entityC2')
    }

    @Test
    void getDataTrunk() {
        def r = get('/core/test/datatrunk', HttpStatus.OK).response.contentAsString
        def dt = JSONUtil.parse(r, DataTrunk.class)
        assert dt.getData().size() == 3
        assert dt.getCount() == 10
    }

    @Test
    void testServiceSupport() {
        assert  c.isPageSearch() == false
        try {
            c.getPageRequest()
        }catch (Exception e){}
    }

    @Test
    void checkDefaultCharsetUsedByStringHttpMessageConverter() {
        def str = '<p>中文乱码</p>'
        assert resOfGet("/core/test/get?str=$str", HttpStatus.OK) == str
    }

    @Test
    void returnList() {
        def result = getAndReturn('/core/test/list', '', HttpStatus.OK)
        def list = JSONUtil.getMapper().readValue(result.toString(), new TypeReference<List<MockEntity>>() { })
        list.each {
            assert it instanceof MockEntity
        }
    }

}
