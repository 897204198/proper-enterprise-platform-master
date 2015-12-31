package org.springframework.util

import spock.lang.Specification
import spock.lang.Unroll

class AntPathMatcherSpec extends Specification {

    @Unroll
    def "Match #url with #pattern is #result"() {
        AntPathMatcher matcher = new AntPathMatcher()

        expect:
        matcher.match(pattern, url) == result

        where:
        result  | pattern                                       | url
        true    | 'GET:/workflow/service/model/*/json'          | 'GET:/workflow/service/model/50/json'
        false   | 'GET:/workflow/service/model/*/json'          | 'GET:/workflow/service/model/5/0/json'
        true    | 'GET:/auth/resources'                         | 'GET:/auth/resources'
        true    | 'GET:/auth/resources/*/*/xml'                 | 'GET:/auth/resources/1/2/xml'
        false   | 'GET:/auth/resources/*/*/xml'                 | 'GET:/auth/resources/12/xml'
        false   | 'GET:/auth/resources/*/*/xml'                 | 'POST:/auth/resources/1/2/xml'
        true    | 'GET:/workflow/service/model/3*/json'         | 'GET:/workflow/service/model/3225/json'
        false   | 'GET:/workflow/service/model/3*/json'         | 'GET:/workflow/service/model/42/json'
        true    | 'GET:/workflow/**'                            | 'GET:/workflow/modeler.html'
        true    | 'GET:/workflow/service/repository/models'     | 'GET:/workflow/service/repository/models'
        true    | '*:/workflow/service/repository/models/*'     | 'GET:/workflow/service/repository/models/123'
        true    | '*:/workflow/service/repository/models/*'     | 'POST:/workflow/service/repository/models/123'
        true    | '*:/workflow/service/repository/models/*'     | 'PUT:/workflow/service/repository/models/123'
        true    | '*:/workflow/service/repository/models/*'     | 'DELETE:/workflow/service/repository/models/123'
        false   | '*:/workflow/service/repository/models/*'     | 'PUT:/workflow/service/repository/models/test/123'
    }

}
