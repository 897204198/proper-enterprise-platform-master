package com.proper.enterprise.platform.core.utils

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ConfCenterSpec extends Specification {

    def "Get '#key' from Config Center is '#value"() {
        expect:
        ConfCenter.get(key) == value

        where:
        key                        | value
        'mode'                     | 'test'
        'key'                      | "i'm in common.properties"
        'port'                     | '27018'
        'core.default_date_format' | 'yyyy-MM-dd'
        'notexist'                 | null
    }

    def "Get '#key' from Config Center is '#value'(default: '#defVal'"() {
        expect:
        ConfCenter.get(key, defVal) == value

        where:
        key         | defVal    | value
        'mode'      | 'abc'     | 'test'
        'notexist'  | 'abc'     | 'abc'
    }

    def "Load from null will throw an exception and catch by ConfCenter"() {
        when:
        ConfCenter.loadProperties(null)

        then:
        notThrown(Exception)
    }

}
