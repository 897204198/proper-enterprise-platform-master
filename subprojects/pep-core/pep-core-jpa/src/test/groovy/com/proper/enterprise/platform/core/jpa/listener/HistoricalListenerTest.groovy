package com.proper.enterprise.platform.core.jpa.listener

import com.proper.enterprise.platform.core.jpa.entity.ManyEntity
import com.proper.enterprise.platform.core.jpa.entity.OneEntity
import com.proper.enterprise.platform.core.jpa.service.ManyService
import com.proper.enterprise.platform.core.jpa.service.OneService
import com.proper.enterprise.platform.core.security.service.SecurityService
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.EntityManager

class HistoricalListenerTest extends AbstractTest {

    @Autowired
    OneService oneService

    @Autowired
    ManyService manyService

    @Autowired
    SecurityService securityService

    @Autowired
    private EntityManager entityManager

    @Test
    void "createUserIdAndLastModifyUserId"() {
        mockUser("test", "test")
        OneEntity oneEntity = new OneEntity().setTest(1)
        oneService.save(oneEntity)
        assert securityService.getCurrentUserId() == oneEntity.getCreateUserId()

        mockUser("test2", "test2")
        oneEntity.setTest(2)
        OneEntity oneput = oneService.save(oneEntity)
        entityManager.flush()
        assert securityService.getCurrentUserId() == oneput.getLastModifyUserId()
    }

    @Test
    void "createUserIdAndLastModifyUserIdWithCascade"() {
        mockUser("test", "test")
        OneEntity oneEntity = new OneEntity().setTest(1)
        ManyEntity manyEntity = new ManyEntity().setTest(1)
        manyEntity.setOneEntity(oneEntity)
        manyService.save(manyEntity)
        assert securityService.getCurrentUserId() == manyEntity.getCreateUserId()
        assert securityService.getCurrentUserId() == manyEntity.getOneEntity().getCreateUserId()
        mockUser("test2", "test2")
        manyEntity.setTest(2)
        manyEntity.setOneEntity(oneEntity.setTest(2))
        ManyEntity manyput = manyService.save(manyEntity)
        entityManager.flush()
        assert securityService.getCurrentUserId() == manyput.getLastModifyUserId()
        assert securityService.getCurrentUserId() == manyput.getOneEntity().getLastModifyUserId()
    }

}
