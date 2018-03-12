package com.proper.enterprise.platform.auth.jpa.aspect

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.jpa.entity.UserEntity
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HistoricalAdviceTest extends AbstractTest {

    def static final MOCK_USER_ID = 'MockUserId'
    def static final DEFAULT_USER_ID = ConfCenter.get("auth.historical.defaultUserId", "PEP_SYS")

    @Autowired
    UserService service

    @Before
    def void mockCurrentUser() {
        mockUser(MOCK_USER_ID)
    }

    @Test
    def void saveEntity() {
        def user = new UserEntity('hinex', 'hinex_password')
        service.save(user)

        def result = service.getByUsername('hinex')
        assert result.getCreateUserId() == MOCK_USER_ID
        assert result.getLastModifyUserId() == MOCK_USER_ID
    }

    @Test
    def void saveEntities() {
        def user1 = new UserEntity('hinex1', 'hinex_password1')
        def user2 = new UserEntity('hinex2', 'hinex_password2')
        service.save(user1, user2)

        assert service.getByUsername('hinex1').getCreateUserId() == MOCK_USER_ID
        assert service.getByUsername('hinex2').getLastModifyUserId() == MOCK_USER_ID
    }

    @Test
    def void onlyUpdateLastModify() {
        service.save new UserEntity('u1', 'p1')
        def user = service.getByUsername 'u1'

        user.setPassword('abc')
        service.save(user)
        user = service.getByUsername 'u1'

        assert user.getLastModifyTime() > user.getCreateTime()
    }

    @Test
    def void thorwExceptionWhenGettingCurrentUser() {
        System.setProperty('test.mockUser.throwEx', 'true')
        ConfCenter.reload()

        service.save(new UserEntity('hinex', 'hinex_password'))

        def result = service.getByUsername('hinex')
        assert result.getCreateUserId() == DEFAULT_USER_ID
        assert result.getLastModifyUserId() == DEFAULT_USER_ID

        System.clearProperty('test.mockUser.throwEx')
        ConfCenter.reload()
    }

}
