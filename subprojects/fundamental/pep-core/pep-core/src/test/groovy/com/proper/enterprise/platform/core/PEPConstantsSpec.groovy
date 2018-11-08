package com.proper.enterprise.platform.core

import spock.lang.Specification

class PEPConstantsSpec extends Specification {

    def 'defaultMonthFormat'() {
        expect:
        'yyyy-MM' == PEPConstants.DEFAULT_MONTH_FORMAT
    }

}
