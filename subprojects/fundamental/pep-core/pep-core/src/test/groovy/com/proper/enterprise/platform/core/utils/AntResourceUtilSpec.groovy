package com.proper.enterprise.platform.core.utils

import spock.lang.Specification

class AntResourceUtilSpec extends Specification {

    def "Support multi-locations separated by comma"() {
        def res = AntResourceUtil.getResources('classpath*:com/proper/**/*.p12,classpath*:com/proper/**/cer*')

        expect:
        res.length == 2 // According to actual files under path pattern
    }

}
