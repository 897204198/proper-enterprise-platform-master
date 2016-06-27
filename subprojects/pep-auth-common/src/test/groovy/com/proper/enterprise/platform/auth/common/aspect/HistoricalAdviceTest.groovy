package com.proper.enterprise.platform.auth.common.aspect
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

class HistoricalAdviceTest extends AbstractTest {

    def static final MOCK_USER_ID = 'MockUserId'

    @Autowired
    @Qualifier('mockUserService')
    UserService service

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
    def void test() {
        System.setProperty('test.throwEx', 'true')
        ConfCenter.reload()

        service.save(new UserEntity('abc', 'def'))
    }

}
