package com.proper.enterprise.platform.auth.common.jpa.listener

import com.proper.enterprise.platform.api.auth.enums.EnableEnum
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity
import com.proper.enterprise.platform.core.security.Authentication
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HistoricalListenerTest extends AbstractTest {

    def static final MOCK_USER_ID = 'MockUserId'

    @Autowired
    UserService service

    @Before
    void mockCurrentUser() {
        mockUser(MOCK_USER_ID)
        Authentication.setCurrentUserId(MOCK_USER_ID)
    }

    @Test
    void saveEntity() {
        def user = new UserEntity('hinex', 'hinex_password')
        service.save(user)

        def result = service.getByUsername('hinex', EnableEnum.ALL)
        assert result.getCreateUserId() == MOCK_USER_ID
        assert result.getLastModifyUserId() == MOCK_USER_ID
    }

    @Test
    void saveEntities() {
        def user1 = new UserEntity('hinex1', 'hinex_password1')
        def user2 = new UserEntity('hinex2', 'hinex_password2')
        service.save(user1, user2)

        assert service.getByUsername('hinex1', EnableEnum.ALL).getCreateUserId() == MOCK_USER_ID
        assert service.getByUsername('hinex2', EnableEnum.ALL).getLastModifyUserId() == MOCK_USER_ID
    }

    @Test
    @NoTx
    void onlyUpdateLastModify() {
        service.save(new UserEntity('u1Last', 'p1'))
        def user = service.getByUsername('u1Last', EnableEnum.ALL)

        user.setPassword('abc')
        service.save(user)
        user = service.getByUsername('u1Last', EnableEnum.ALL)

        assert user.getLastModifyTime() > user.getCreateTime()
    }

    @Test
    void thorwExceptionWhenGettingCurrentUser() {
        System.setProperty('test.mockUser.throwEx', 'true')
        ConfCenter.reload()

        service.save(new UserEntity('hinex', 'hinex_password'))

        def result = service.getByUsername('hinex', EnableEnum.ALL)
        assert result.getCreateUserId() == MOCK_USER_ID
        assert result.getLastModifyUserId() == MOCK_USER_ID

        System.clearProperty('test.mockUser.throwEx')
        ConfCenter.reload()
    }

}
