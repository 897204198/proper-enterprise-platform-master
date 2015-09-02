package com.proper.enterprise.platform.core.utils

import spock.lang.Specification
import spock.lang.Unroll

class StringUtilSpec extends Specification {

    @Unroll
    def "The result of clean #url is #result"() {
        expect:
        StringUtil.cleanUrl(url) == result

        where:
        url             | result
        '/test?a=1&b=2' | '/test'
        '/test'         | '/test'
        '/'             | '/'
        '/?_=342'       | '/'
        'abc'           | 'abc'
        null            | ''
    }

}
