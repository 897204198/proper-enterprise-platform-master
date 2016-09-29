package com.proper.enterprise.platform.configs.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.configs.dal.entity.AEntity
import org.junit.Test
import org.springframework.http.HttpStatus

class TestControllerTest extends AbstractTest {

    @Test
    public void checkDefaultCharsetUsedByStringHttpMessageConverter() {
        def str = '<p>中文乱码</p>'
        def result = getAndReturn("/test/get?str=$str", '', HttpStatus.OK)
        assert result == str
    }

    @Test
    public void returnList() {
        def result = getAndReturn('/test/list', '', HttpStatus.OK)
        def list = JSONUtil.getMapper().readValue(result.toString(), new TypeReference<List<AEntity>>() { })
        list.each {
            assert it instanceof AEntity
        }
    }

}
