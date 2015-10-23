package org.springframework.util

import spock.lang.Specification

class AntPathMatcherSpec extends Specification {

    def "Match #path with #url is #result"() {
        AntPathMatcher matcher = new AntPathMatcher()

        expect:
        matcher.match(url, path) == result

        where:
        result  | url                                   | path
        true    | 'GET:/workflow/service/model/*/json'  | 'GET:/workflow/service/model/50/json'
        false   | 'GET:/workflow/service/model/*/json'  | 'GET:/workflow/service/model/5/0/json'
        true    | 'GET:/auth/resources'                 | 'GET:/auth/resources'
        true    | 'GET:/auth/resources/*/*/xml'         | 'GET:/auth/resources/1/2/xml'
        false   | 'GET:/auth/resources/*/*/xml'         | 'GET:/auth/resources/12/xml'
        false   | 'GET:/auth/resources/*/*/xml'         | 'POST:/auth/resources/1/2/xml'
        true    | 'GET:/workflow/service/model/3*/json' | 'GET:/workflow/service/model/3225/json'
        false   | 'GET:/workflow/service/model/3*/json' | 'GET:/workflow/service/model/42/json'
        true    | 'GET:/workflow/**'                    | 'GET:/workflow/modeler.html'
    }

}
