package com.proper.enterprise.platform.integration.auth.aop

import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.core.conf.ConfManager
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser

class HistoricalAdviceIntegTest extends AbstractIntegTest {

    def static final MOCK_USER_ID = ConfManager.getString('auth.common', 'test.mock.userId', 'dc65766c-0176-4a1e-ad0e-dd06ba645c7l')
    
    @Autowired
    UserRepository repo
    
    @Test
    @WithMockUser('admin')
    def void saveEntity() {
        def user = repo.save(new UserEntity('hinex', 'hinex_password'))
        assert user.createUserId == MOCK_USER_ID
        assert user.lastModifyUserId == MOCK_USER_ID
    }

    @Test
    @WithMockUser('admin')
    def void saveEntities() {
        def user1 = new UserEntity('hinex1', 'hinex_password1')
        def user2 = new UserEntity('hinex2', 'hinex_password2')
        repo.save([user1, user2])
        assert user1.createUserId == MOCK_USER_ID
        assert user2.lastModifyUserId == MOCK_USER_ID
    }

}
