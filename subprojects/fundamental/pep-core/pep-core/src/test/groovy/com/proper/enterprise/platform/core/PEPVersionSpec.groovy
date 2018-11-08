package com.proper.enterprise.platform.core

import spock.lang.Specification

class PEPVersionSpec extends Specification {

    def 'check version'() {
        expect:
        PEPVersion.getVersion().hashCode() == PEPVersion.VERSION
    }

}
