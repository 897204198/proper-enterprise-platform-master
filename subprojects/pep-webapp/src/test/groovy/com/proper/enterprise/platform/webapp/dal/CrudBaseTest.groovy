package com.proper.enterprise.platform.webapp.dal

import com.proper.enterprise.platform.api.auth.enums.UseStatus
import com.proper.enterprise.platform.webapp.dal.entity.AEntity
import com.proper.enterprise.platform.webapp.dal.repository.ARepository
import com.proper.enterprise.platform.test.integration.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class CrudBaseTest extends AbstractTest {
    
    @Autowired
    ARepository repository
    
    String id
    
    void create() {
        AEntity user = new AEntity()
        user.setUsername('test')
        user.setPassword('testpassword')
        repository.save(user)
        id = user.getId()
    }
    
    void retrieve() {
        AEntity user = repository.findOne(id)
        
        // check set values
        assert user.createUserId > ''
        assert user.lastModifyUserId > ''
        assert user.username == 'test'
        assert user.password == 'testpassword'
        
        // check default values
        assert user.useStatus == UseStatus.STOP
    }
    
    void update() {
        AEntity user = repository.findOne(id)
        user.description = 'desc of user1'
        repository.save(user)
    }
    
    void updateCheck() {
        AEntity user = repository.findOne(id)
        assert user.description == 'desc of user1'
    }

    void delete() {
        repository.delete(id)
    }

    @Test
    void testNothing() { }

}
