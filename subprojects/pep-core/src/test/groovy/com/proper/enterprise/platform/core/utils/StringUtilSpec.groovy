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

    @Unroll
    def "'#input' is #result"() {
        expect:
        result == (StringUtil.isNull(input) ? 'null' : 'not null')

        where:
        input   | result
        ''      | 'null'
        ' '     | 'null'
        ' ab '  | 'not null'
    }

    @Unroll
    def "Join #input with #separator is #result"() {
        expect:
        result == StringUtil.join(input, separator)

        where:
        separator   | input         | result
        '.'         | ['a', 'b']    | 'a.b'
        ''          | []            | ''
        null        | []            | ''
        ''          | ['a', 'b']    | 'ab'
        ','         | ['a']         | 'a'
    }

}
