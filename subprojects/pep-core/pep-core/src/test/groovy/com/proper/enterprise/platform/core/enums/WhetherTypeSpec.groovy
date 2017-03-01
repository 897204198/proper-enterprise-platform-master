package com.proper.enterprise.platform.core.enums

import spock.lang.Specification
import spock.lang.Unroll


class WhetherTypeSpec extends Specification {

    @Unroll
    def "The code of #type is #code"() {
        expect:
        WhetherType.codeOf(code) == type

        where:
        code    | type
        1       | WhetherType.YES
        3       | null
    }

    def "Test valueOf"() {
        expect:
        WhetherType.valueOf('YES') == WhetherType.YES
    }

}
