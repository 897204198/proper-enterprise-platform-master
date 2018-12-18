package com.proper.enterprise.platform.core.jpa

import com.proper.enterprise.platform.test.AbstractJPATest
import com.proper.enterprise.platform.core.jpa.entity.AOEntity
import com.proper.enterprise.platform.core.jpa.repository.AORepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class CrudBaseTest extends AbstractJPATest {

    @Autowired
    AORepository repository

    String id

    void create() {
        AOEntity user = new AOEntity()
        user.setUsername('中文')
        user.setPassword('testpassword')
        repository.save(user)
        id = user.getId()
    }

    void retrieve() {
        AOEntity user = repository.findById(id).orElse(null)

        // check set values
        assert user.createUserId > ''
        assert user.lastModifyUserId > ''
        assert user.username == '中文'
        assert user.password == 'testpassword'

    }

    void update() {
        AOEntity user = repository.findById(id).orElse(null)
        user.description = 'desc of user1'
        repository.save(user)
    }

    void updateCheck() {
        AOEntity user = repository.findById(id).orElse(null)
        assert user.description == 'desc of user1'
    }

    def delete() {
       return repository.delete(id)
    }

    @Test
    void testNothing() { }

}
