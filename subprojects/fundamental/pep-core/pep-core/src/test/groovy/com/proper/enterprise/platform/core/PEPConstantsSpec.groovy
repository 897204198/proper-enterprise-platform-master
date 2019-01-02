package com.proper.enterprise.platform.core

import spock.lang.Specification

class PEPConstantsSpec extends Specification {

    def 'defaultMonthFormat'() {
        expect:
        'yyyy-MM' == PEPPropertiesLoader.load(CoreProperties.class).getDefaultMonthFormat()
    }

}
