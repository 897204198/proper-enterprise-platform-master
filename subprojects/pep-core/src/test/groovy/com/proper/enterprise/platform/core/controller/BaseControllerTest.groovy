package com.proper.enterprise.platform.core.controller
import com.proper.enterprise.platform.core.repository.mock.entity.MockEntity
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class BaseControllerTest extends AbstractTest {

    def c = new BaseController() {}
    def entity = new MockEntity('abc')

    @Test
    public void testPOSTResponse() {
        def result = c.responseOfPost(entity)
        assert result.getStatusCode() == HttpStatus.CREATED
        assert result.getBody() == entity
    }

    @Test
    public void testGETResponse() {
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

        result = c.responseOfGet([])
        assert result.getStatusCode() == HttpStatus.OK
        assert result.getBody() == null
    }

    @Test
    public void testPUTResponse() {
        // Same internal method as responseOfGet
        def result = c.responseOfPut(entity)
        assert result.getStatusCode() == HttpStatus.OK
        assert result.getBody() == entity
    }

    @Test
    public void testDELETEResponse() {
        def result = c.responseOfDelete(true)
        assert result.getStatusCode() == HttpStatus.NO_CONTENT
        assert result.getBody() == null

        result = c.responseOfDelete(false)
        assert result.getStatusCode() == HttpStatus.NOT_FOUND
        assert result.getBody() == null
    }

    @Test
    public void handleTrouble() {
        def textPlainUtf8 = MediaType.TEXT_PLAIN_VALUE + ';charset=UTF-8'

        def r = get('/test/trouble/1?div=1', HttpStatus.OK)
        assert r.getResponse().getContentType() == MediaType.APPLICATION_JSON_UTF8_VALUE

        def r1 = get('/test/trouble/1?div=0', HttpStatus.BAD_REQUEST)
        assert 'Division by zero' == r1.getResponse().getContentAsString()
        assert r1.getResponse().getContentType() == textPlainUtf8
        def r2 = get('/test/trouble/1?div=abc', HttpStatus.BAD_REQUEST)
        assert 'For input string: "abc"' == r2.getResponse().getContentAsString()

        def r3 = get('/test/trouble/2', HttpStatus.BAD_REQUEST)
        assert '异常啦' == r3.getResponse().getContentAsString()
        assert r3.getResponse().getContentType() == textPlainUtf8

        get('/test/trouble/3', HttpStatus.NOT_FOUND)
    }

    @Test
    public void ignorePropertyInEntity() {
        def r = get('/test/json/entity', HttpStatus.OK).getResponse().getContentAsString()
        def m = JSONUtil.parse(r, Map.class)
        assert m.size() == 3
        assert m.containsKey('id')
        assert m.containsKey('lastModifyTime')
        assert m.containsKey('entityC2')
    }

}
