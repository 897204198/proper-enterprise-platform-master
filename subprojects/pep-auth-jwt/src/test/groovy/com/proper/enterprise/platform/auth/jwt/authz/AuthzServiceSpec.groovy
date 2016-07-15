package com.proper.enterprise.platform.auth.jwt.authz

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class AuthzServiceSpec extends Specification {

    @Shared static AuthzService service = new AuthzService()

    def setupSpec() {
        def list = []
        list.with {
            push 'GET:/auth/resources'
            push 'GET:/workflow/service/model/*/json'
            push 'GET:/auth/resources/*/*/xml'
            push 'GET:/workflow/service/model/3*/json'
            push 'GET:/workflow/*.html'
            push 'POST:/auth/login'
        }
        service.setIgnorePatterns(list)
    }

    @Unroll
    def "#method #url should ignore? #result"() {
        expect:
        result == service.shouldIgnore(url, method, hasContext)

        where:
        hasContext  | result | method | url
        false       | true   | 'GET'  | '/workflow/service/model/50/json'
        false       | false  | 'GET'  | '/workflow/service/model/5/0/json'
        false       | true   | 'GET'  | '/auth/resources'
        false       | true   | 'GET'  | '/auth/resources?name=1&type=2'
        false       | true   | 'GET'  | '/auth/resources/1/2/xml'
        false       | false  | 'GET'  | '/auth/resources/12/xml'
        false       | false  | 'POST' | '/auth/resources/1/2/xml'
        false       | true   | 'GET'  | '/workflow/service/model/3225/json'
        false       | true   | 'GET'  | '/workflow/modeler.html'
        true        | true   | 'POST' | '/pep/auth/login'
        true        | false  | 'GET'  | '/pep/auth/login'
    }

}
