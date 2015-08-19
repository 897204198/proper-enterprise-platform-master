package com.proper.enterprise.platform.integration.auth.aspect

import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class HistoricalAdviceIntegTest extends AbstractIntegTest {
    
    @Autowired
    UserRepository repo
    
    @Test
    def void saveEntity() {
        def user = repo.save(new UserEntity('hinex', 'hinex_account', 'hinex_password'))
        assert user.createUserId == 'aop'
        assert user.lastModifyUserId == 'aop'
    }

    @Test
    def void saveEntities() {
        def user1 = new UserEntity('hinex1', 'hinex_account1', 'hinex_password1')
        def user2 = new UserEntity('hinex2', 'hinex_account2', 'hinex_password2')
        repo.save([user1, user2])
        assert user1.createUserId == 'aop'
        assert user2.lastModifyUserId == 'aop'
    }

}
