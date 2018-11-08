package com.proper.enterprise.platform.core.jpa.listener

import com.proper.enterprise.platform.core.jpa.entity.ManyEntity
import com.proper.enterprise.platform.core.jpa.entity.OneEntity
import com.proper.enterprise.platform.core.jpa.service.ManyService
import com.proper.enterprise.platform.core.jpa.service.OneService
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HistoricalListenerTest extends AbstractJPATest {

    @Autowired
    OneService oneService

    @Autowired
    ManyService manyService

    @Test
    void "createUserIdAndLastModifyUserId"() {
        mockUser("test", "test")
        Authentication.setCurrentUserId("test")
        OneEntity oneEntity = new OneEntity().setTest(1)
        oneService.save(oneEntity)
        assert Authentication.getCurrentUserId() == oneEntity.getCreateUserId()

        mockUser("test2", "test2")
        oneEntity.setTest(2)
        OneEntity oneput = oneService.save(oneEntity)
        assert Authentication.getCurrentUserId() == oneput.getLastModifyUserId()
    }

    @Test
    void "createUserIdAndLastModifyUserIdWithCascade"() {
        mockUser("test", "test")
        OneEntity oneEntity = new OneEntity().setTest(1)
        ManyEntity manyEntity = new ManyEntity().setTest(1)
        manyEntity.setOneEntity(oneEntity)
        manyService.save(manyEntity)
        assert Authentication.getCurrentUserId() == manyEntity.getCreateUserId()
        assert Authentication.getCurrentUserId() == manyEntity.getOneEntity().getCreateUserId()
        mockUser("test2", "test2")
        manyEntity.setTest(2)
        manyEntity.setOneEntity(oneEntity.setTest(2))
        ManyEntity manyput = manyService.save(manyEntity)
        assert Authentication.getCurrentUserId() == manyput.getLastModifyUserId()
        assert Authentication.getCurrentUserId() == manyput.getOneEntity().getLastModifyUserId()
    }

}
