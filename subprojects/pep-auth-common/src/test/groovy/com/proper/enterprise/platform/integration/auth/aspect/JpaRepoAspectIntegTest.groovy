package com.proper.enterprise.platform.integration.auth.aspect

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.test.integration.annotation.IntegTest

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration('/spring/auth/common/applicationContext-auth-common.xml')
@IntegTest
class JpaRepoAspectIntegTest {
    
    @Autowired
    UserRepository repo
    
    @Test
    def void saveEntity() {
        def user = repo.save(new UserEntity('hinex', 'hinex_account', 'hinex_password'))
    }

}
