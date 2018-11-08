package com.proper.enterprise.platform.core.utils

import com.proper.enterprise.platform.core.utils.encrypt.EncryptUtil
import spock.lang.Specification

class EncryptUtilSpec extends Specification {

    def "encryptEmail"() {
        expect:
        assert "tes***@qq.com" == EncryptUtil.encryptEmail("test@qq.com")
        assert "123@qq.com" == EncryptUtil.encryptEmail("123@qq.com")
        assert "testQQ.com" == EncryptUtil.encryptEmail("testQQ.com")
    }
}
