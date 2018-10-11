package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.utils.valid.ValidUtil
import spock.lang.Specification

class ValidUtilSpec extends Specification {

    def "validEmail"() {
        expect:
        assert ValidUtil.isEmail("test@qq.com")
        assert !ValidUtil.isEmail("testQQ.com")
    }
}
