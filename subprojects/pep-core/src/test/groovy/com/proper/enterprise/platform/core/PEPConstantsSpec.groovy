package com.proper.enterprise.platform.core

import spock.lang.Specification


class PEPConstantsSpec extends Specification {

    def "Check PEP constants"() {
        expect:
        PEPConstants.DEFAULT_CHARSET.name() == 'UTF-8'
    }

}
