package com.proper.enterprise.platform.core.i18n

import com.proper.enterprise.platform.test.AbstractSpringTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class I18NServiceTest extends AbstractSpringTest {

    @Autowired
    private I18NService service

    @Test
    void equalityWithUtil() {
        def key = 'core.test.key'
        def k2 = 'core.test.k2'
        def baseName = 'i18n.core.core-i18n'
        def ins = I18NUtil.getInstance(baseName)
        assert service.getMessage(key) == I18NUtil.getString(baseName, key)
        assert service.getMessage(k2) == ins.getString(k2)
    }

    @Test
    void test() {
        def msg = service.getMessage('core.test.tpl',
                                     new Date().format('yyyy年MM月dd日'), new Date().format('u'))
        assert msg.length() > 4 + 2 + 2 + 1
    }

}
