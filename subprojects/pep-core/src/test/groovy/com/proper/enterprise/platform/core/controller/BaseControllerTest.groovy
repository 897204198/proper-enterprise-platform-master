package com.proper.enterprise.platform.core.controller

import com.proper.enterprise.platform.core.repository.entity.MockEntity
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.http.HttpStatus


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
        assert result.getStatusCode() == HttpStatus.NOT_FOUND
        assert result.getBody() == null

        result = c.responseOfGet([])
        assert result.getStatusCode() == HttpStatus.NOT_FOUND
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

}
