package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.proper.enterprise.platform.api.authc.entity.UserEntity
import com.proper.enterprise.platform.integration.IntegTest
import com.proper.enterprise.platform.integration.webapp.dal.repository.BaseDAOTestRepository

@RunWith(SpringJUnit4ClassRunner.class)
@IntegTest
class UniqueIntegTest {
    
    @Autowired
    BaseDAOTestRepository repository
    
    static String id
    
    @Test(expected=DataIntegrityViolationException)
    void duplicateUniqueCol() {
        create()
        create()
    }
    
    void create() {
        UserEntity user = new UserEntity()
        user.setCreateUserId('1')
        user.setLastModifyUserId('1')
        user.setLoginName('test')
        user.setAccount('testaccount')
        user.setPassword('testpassword')
        repository.save(user)
        id = user.id
    }
    
    @After
    void cleanUp() {
       repository.delete(id) 
    }
    
}
