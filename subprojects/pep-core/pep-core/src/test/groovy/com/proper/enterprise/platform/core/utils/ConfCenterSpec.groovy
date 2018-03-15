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
        'host'                     | 'localhost'
    }

    def "Get '#key' from Config Center is '#value'(default: '#defVal'"() {
        expect:
        ConfCenter.get(key, defVal) == value

        where:
        key         | defVal    | value
        'mode'      | 'abc'     | 'test'
        'notexist'  | 'abc'     | 'abc'
    }

    def "Get int property as expected"() {
        expect:
        ConfCenter.getInt('priority') == 16
        ConfCenter.getInt('not.exist', 23) == 23

        when:
        ConfCenter.getInt('not.exist')

        then:
        thrown(NumberFormatException)
    }

    def "Load from null will throw an exception and catch by ConfCenter"() {
        when:
        ConfCenter.loadProperties(null)

        then:
        notThrown(Exception)
    }

    def "Check loading parameters in fixed order"() {
        given:
        // *nix has PATH
        System.setProperty("PATH", "value-in-sys")
        // and Windows has Path in environment
        System.setProperty("Path", "value-in-sys")
        System.setProperty("pep.sys.param", "value-in-sys")
        ConfCenter.reload()

        expect:
        // Config center should get USER from environment variables
        ConfCenter.get("PATH") != "value-in-sys"        || ConfCenter.get('Path') != 'value-in-sys'
        ConfCenter.get("PATH") != "value-in-properties" || ConfCenter.get('Path') != 'value-in-properties'
        // and should get pep.sys.param from system properties
        ConfCenter.get("pep.sys.param") == "value-in-sys"
    }

}
