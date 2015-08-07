package com.proper.enterprise.platform.integration.webapp.dao

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.proper.enterprise.platform.api.authc.entity.UserEntity
import com.proper.enterprise.platform.integration.IntegTest
import com.proper.enterprise.platform.integration.webapp.dao.repository.UserRepository

@RunWith(SpringJUnit4ClassRunner.class)
@IntegTest
class CrudIntegrationTest {
    
    @Autowired
    UserRepository repository
    
    @Test
    void crud() {
        UserEntity user = new UserEntity()
        user.setCreateUserId('1')
        user.setLastModifyUserId('1')
        user.setLoginName('test')
        user.setAccount('testaccount')
        user.setPassword('testpassword')
        repository.save(user)
    }

}
