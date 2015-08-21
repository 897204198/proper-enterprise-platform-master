package com.proper.enterprise.platform.integration.webapp.dal

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import com.proper.enterprise.platform.core.enums.ActiveStatus
import com.proper.enterprise.platform.core.enums.UseStatus
import com.proper.enterprise.platform.integration.webapp.dal.entity.TestEntity
import com.proper.enterprise.platform.integration.webapp.dal.repository.TestRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest

class CrudBaseIntegTest extends AbstractIntegTest {
    
    @Autowired
    TestRepository repository
    
    String id
    
    void create() {
        TestEntity user = new TestEntity()
        user.setLoginName('test')
        user.setAccount('testaccount')
        user.setPassword('testpassword')
        repository.save(user)
        id = user.getId()
    }
    
    void retrieve() {
        TestEntity user = repository.findOne(id)
        
        // check set values
        assert user.createUserId > ''
        assert user.lastModifyUserId > ''
        assert user.loginName == 'test'
        assert user.account == 'testaccount'
        assert user.password == 'testpassword'
        
        // check default values
        assert user.activeStatus == ActiveStatus.INACTIVE
        assert user.useStatus == UseStatus.STOP
        assert !user.neverExpired
    }
    
    void update() {
        TestEntity user = repository.findOne(id)
        user.description = 'desc of user1'
        repository.save(user)
    }
    
    void updateCheck() {
        TestEntity user = repository.findOne(id)
        assert user.description == 'desc of user1'
    }

    void delete() {
        repository.delete(id)
    }

    @Test
    void testNothing() { }

}
