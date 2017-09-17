package com.proper.enterprise.platform.sys.i18n

import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class I18NServiceTest extends AbstractTest {

    @Autowired
    private I18NService service

    @Test
    void equalityWithUtil() {
        def key = 'sys.test.key'
        def k2 = 'sys.test.k2'
        def baseName = 'i18n.sys.sys-i18n'
        def ins = I18NUtil.getInstance(baseName)
        assert service.getMessage(key) == I18NUtil.getString(baseName, key)
        assert service.getMessage(k2) == ins.getString(k2)
    }

}
