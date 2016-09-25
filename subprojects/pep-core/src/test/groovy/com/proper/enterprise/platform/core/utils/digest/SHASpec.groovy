package com.proper.enterprise.platform.core.utils.digest

import spock.lang.Specification

class SHASpec extends Specification {

    def "#content after sha256 is #result"() {
        expect:
        result == SHA.sha256(content)

        where:
        result                                                             | content
        '6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090' | 'abc123'
    }

}
