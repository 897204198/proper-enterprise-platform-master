package com.proper.enterprise.platform.notice.server.push.repository

import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.notice.server.sdk.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException

class PushConfigMongoRepositoryTest extends AbstractJPATest {

    @Autowired
    PushConfigMongoRepository pushConfigMongoRepository

    @Test
    public void testUniqueIndex() {
        PushConfDocument pushConf = new PushConfDocument()
        pushConf.setAppKey("testPushConfSave")
        pushConf.setPushPackage("testPushConfSave")
        pushConf.setPushChannel(PushChannelEnum.APNS)
        pushConfigMongoRepository.save(pushConf)
        PushConfDocument pushConf2 = new PushConfDocument()
        pushConf2.setAppKey("testPushConfSave")
        pushConf2.setPushPackage("testPushConfSave")
        pushConf2.setPushChannel(PushChannelEnum.APNS)
        try {
            pushConfigMongoRepository.save(pushConf2)
        } catch (DuplicateKeyException e) {
            print "valid success"
        }

    }
}
