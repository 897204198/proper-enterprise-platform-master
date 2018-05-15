package com.proper.enterprise.platform.core.jpa

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.core.jpa.entity.AEntity
import com.proper.enterprise.platform.core.jpa.repository.ARepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class CrudBaseTest extends AbstractTest {

    @Autowired
    ARepository repository

    String id

    void create() {
        AEntity user = new AEntity()
        user.setUsername('中文')
        user.setPassword('testpassword')
        repository.save(user)
        id = user.getId()
    }

    void retrieve() {
        AEntity user = repository.findOne(id)

        // check set values
        assert user.createUserId > ''
        assert user.lastModifyUserId > ''
        assert user.username == '中文'
        assert user.password == 'testpassword'

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
