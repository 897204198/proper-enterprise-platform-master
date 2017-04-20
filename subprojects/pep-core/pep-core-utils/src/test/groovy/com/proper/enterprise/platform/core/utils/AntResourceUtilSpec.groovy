package com.proper.enterprise.platform.core.utils

import spock.lang.Specification

class AntResourceUtilSpec extends Specification {

    def "Support multi-locations separated by comma"() {
        def res = AntResourceUtil.getResources('classpath*:conf/common/**/*.properties,classpath*:com/proper/**/*.p12')

        expect:
        res.length == 3 // According to actual files under src/test/resources
    }

}
