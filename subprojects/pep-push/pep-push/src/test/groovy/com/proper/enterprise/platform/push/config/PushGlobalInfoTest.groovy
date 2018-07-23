package com.proper.enterprise.platform.push.config.jms

import com.proper.enterprise.platform.push.config.PushGlobalInfo
import com.proper.enterprise.platform.push.test.PushAbstractTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class PushGlobalInfoTest extends PushAbstractTest {

    @Autowired
    PushGlobalInfo pushGlobalInfo

    def vo

    @Before
    void beforeInit() {
        vo = initData()
    }

    @After
    void afterData() {
        delete(vo.getId())
    }

    @Test
    void getPushConfigsTest() {
        def configs = pushGlobalInfo.getPushConfigs()
        assert configs.size() == 1

        vo.setAndroid(null)
        assert configs.size() == 1
    }


}
