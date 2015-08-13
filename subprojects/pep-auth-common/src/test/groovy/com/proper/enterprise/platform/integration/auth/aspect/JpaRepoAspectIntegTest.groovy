package com.proper.enterprise.platform.integration.auth.aspect

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextHierarchy

import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest

@ContextHierarchy(@ContextConfiguration('/spring/auth/common/applicationContext-auth-common.xml'))
class JpaRepoAspectIntegTest extends AbstractIntegTest {
    
    @Autowired
    UserRepository repo
    
    @Test
    def void saveEntity() {
        def user = repo.save(new UserEntity('hinex', 'hinex_account', 'hinex_password'))
    }

}
