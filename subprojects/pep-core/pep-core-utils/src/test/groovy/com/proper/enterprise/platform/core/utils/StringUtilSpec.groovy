package com.proper.enterprise.platform.core.utils

import spock.lang.Specification
import spock.lang.Unroll

class StringUtilSpec extends Specification {

    @Unroll
    def "'#input' is null? #result"() {
        expect:
        result == StringUtil.isNull(input)

        where:
        input   | result
        ''      | true
        ' '     | true
        ' ab '  | false
    }

    @Unroll
    def "'#input' is not null? #result"() {
        expect:
        result == StringUtil.isNotNull(input)

        where:
        input   | result
        ''      | false
        ' '     | false
        ' ab '  | true
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
    
    @Unroll
    def "CamelCase'#input' camelToSnake is #result"(){
        expect:
        result == StringUtil.camelToSnake (input)
        where:
        input                |           result
        'likeThis'           |           'like_this'
        'MyTest'             |           'my_test'
        'TestTest'           |           'test_test'
    }

}
