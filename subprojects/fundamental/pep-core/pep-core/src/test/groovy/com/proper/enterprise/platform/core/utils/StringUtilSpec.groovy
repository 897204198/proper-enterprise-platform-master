package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset

class StringUtilSpec extends Specification {

    @Unroll
    def "'#input' is null? #result"() {
        expect:
        result == StringUtil.isNull(input)

        where:
        input  | result
        ''     | true
        ' '    | true
        ' ab ' | false
    }

    @Unroll
    def "'#input' is not null? #result"() {
        expect:
        result == StringUtil.isNotNull(input)

        where:
        input  | result
        ''     | false
        ' '    | false
        ' ab ' | true
    }

    @Unroll
    def "Join #input with #separator is #result"() {
        expect:
        result == StringUtil.join(input, separator)

        where:
        separator | input      | result
        '.'       | ['a', 'b'] | 'a.b'
        ''        | []         | ''
        null      | []         | ''
        ''        | ['a', 'b'] | 'ab'
        ','       | ['a']      | 'a'
    }

    @Unroll
    def "CamelCase'#input' camelToSnake is #result"() {
        expect:
        result == StringUtil.camelToSnake(input)
        where:
        input      | result
        'likeThis' | 'like_this'
        'MyTest'   | 'my_test'
        'TestTest' | 'test_test'
    }

    def "test toEncodedString"() {
        expect:
        '这个是中文' == StringUtil.toEncodedString('这个是中文'.getBytes(PEPPropertiesLoader.load(CoreProperties.class)
            .getCharset()))
        '这个是中文' != StringUtil.toEncodedString('这个是中文'.getBytes(Charset.forName("GBK")))
        '' == StringUtil.toEncodedString(null)
    }
}
