package com.proper.enterprise.platform.notice.server.push.repository

import com.proper.enterprise.platform.notice.server.push.dao.document.PushConfDocument
import com.proper.enterprise.platform.notice.server.push.dao.repository.PushConfigMongoRepository
import com.proper.enterprise.platform.notice.server.push.enums.PushChannelEnum
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException

class PushConfigMongoRepositoryTest extends AbstractTest {

    @Autowired
    PushConfigMongoRepository pushConfigMongoRepository

    @Test
    public void testUniqueIndex() {
        PushConfDocument pushConf = new PushConfDocument()
        pushConf.setAppKey("testPushConfSave")
        pushConf.setPushPackage("testPushConfSave")
        pushConf.setPushChannel(PushChannelEnum.IOS)
        pushConfigMongoRepository.save(pushConf)
        PushConfDocument pushConf2 = new PushConfDocument()
        pushConf2.setAppKey("testPushConfSave")
        pushConf2.setPushPackage("testPushConfSave")
        pushConf2.setPushChannel(PushChannelEnum.IOS)
        try {
            pushConfigMongoRepository.save(pushConf2)
        } catch (DuplicateKeyException e) {
            print "valid success"
        }

    }
}
