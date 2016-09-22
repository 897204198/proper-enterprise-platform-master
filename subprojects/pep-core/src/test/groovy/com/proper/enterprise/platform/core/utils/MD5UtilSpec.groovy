package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.utils.digest.MD5
import spock.lang.Specification
import spock.lang.Unroll


class MD5UtilSpec extends Specification {

    @Unroll
    def "MD5 HEX string of #input is #result"() {
        expect:
        result == MD5.md5Hex(input)

        where:
        result                              | input
        'e10adc3949ba59abbe56e057f20f883e'  | '123456'
    }

}
