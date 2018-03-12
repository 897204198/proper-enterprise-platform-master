package com.proper.enterprise.platform.auth.neo4j.service.impl

import com.proper.enterprise.platform.auth.common.service.impl.ResourceServiceImpl
import com.proper.enterprise.platform.auth.neo4j.entity.ResourceNodeEntity
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.web.bind.annotation.RequestMethod.GET

class ResourceNodeServiceImplTest extends Specification {

    @Unroll
    def "Find best matches url pattern to #signature is #result"() {
        def service = new ResourceServiceImpl()

        expect:
        result == service.getBestMatch(constructCollection(resources), signature).url

        where:
        result                     | signature                              | resources
        '/classes/*/*/group/*'     | 'GET:/classes/Archive/1/group/2'       | ['/classes/**', '/classes/*/*/group/*']
        '/classes/Archive/*'       | 'GET:/classes/Archive/123'             | ['/classes/**', '/classes/Archive/*']
        '/classes/Archive/name/**' | 'GET:/classes/Archive/name/11111/1234' | ['/classes/**/name/11111/*', '/classes/Archive/name/**']
        '/classes/Archive/name/'   | 'GET:/classes/Archive/name/'           | ['/classes/Archive/name/', '/classes/Archive/name/**']
    }

    def constructCollection(urls) {
        def list = []
        urls.each { url ->
            list.add new ResourceNodeEntity(url, GET)
        }
        list
    }

}
