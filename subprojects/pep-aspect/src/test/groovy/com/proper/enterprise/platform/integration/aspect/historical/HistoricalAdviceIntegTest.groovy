package com.proper.enterprise.platform.integration.aspect.historical

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.core.conf.ConfManager
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser

class HistoricalAdviceIntegTest extends AbstractIntegTest {

    def static final MOCK_USER_ID = ConfManager.getString('auth.common', 'test.mock.userId', 'dc65766c-0176-4a1e-ad0e-dd06ba645c7l')
    
    @Autowired UserService service
    
    @Test
    @WithMockUser('admin')
    def void saveEntity() {
        def user = new UserEntity('hinex', 'hinex_password')
        service.addUser(user)

        def result = service.getUser('hinex')
        assert result.getCreateUserId() == MOCK_USER_ID
        assert result.getLastModifyUserId() == MOCK_USER_ID
    }

    @Test
    @WithMockUser('admin')
    def void saveEntities() {
        def user1 = new UserEntity('hinex1', 'hinex_password1')
        def user2 = new UserEntity('hinex2', 'hinex_password2')
        service.addUser(user1, user2)

        assert service.getUser('hinex1').getCreateUserId() == MOCK_USER_ID
        assert service.getUser('hinex2').getLastModifyUserId() == MOCK_USER_ID
    }

}
