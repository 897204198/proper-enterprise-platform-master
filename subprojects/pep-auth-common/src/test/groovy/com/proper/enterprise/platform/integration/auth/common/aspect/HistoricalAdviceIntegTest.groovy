package com.proper.enterprise.platform.integration.auth.common.aspect
import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HistoricalAdviceIntegTest extends AbstractIntegTest {

    def static final MOCK_USER_ID = 'MockUserId'
    
    @Autowired UserService service
    
    @Test
    def void saveEntity() {
        def user = new UserEntity('hinex', 'hinex_password')
        service.addUser(user)

        def result = service.getUser('hinex')
        assert result.getCreateUserId() == MOCK_USER_ID
        assert result.getLastModifyUserId() == MOCK_USER_ID
    }

    @Test
    def void saveEntities() {
        def user1 = new UserEntity('hinex1', 'hinex_password1')
        def user2 = new UserEntity('hinex2', 'hinex_password2')
        service.addUser(user1, user2)

        assert service.getUser('hinex1').getCreateUserId() == MOCK_USER_ID
        assert service.getUser('hinex2').getLastModifyUserId() == MOCK_USER_ID
    }

}
