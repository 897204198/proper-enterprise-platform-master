package com.proper.enterprise.platform.auth.common.controller

import com.proper.enterprise.platform.auth.common.repository.ResourceRepository
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class ResourcesControllerTest extends AbstractTest {

    @Autowired
    ResourceRepository repository

    @Test
    public void test() {
        post('/auth/resources', '{"url":"/foo/bar", "method": "PUT"}', HttpStatus.OK)
        // TODO url + method 的联合主键没生效？
        post('/auth/resources', '{"url":"/foo/bar", "method": "PUT"}', HttpStatus.OK)

//        println "="*50
//        repository.findAll().each {
//            println it
//        }
    }

}
