package com.proper.enterprise.platform.integration.auth.common.entity

import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser

class UserEntityIntegTest extends AbstractIntegTest {

    @Autowired
    UserRepository repo

    @Test
    @WithMockUser('admin')
    public void checkUserExtendProperties() {
        UserEntity entity = new UserEntity('hinex', '123456')
        entity.putExtendProperty('a', '1')
        entity.putExtendProperty('b', '2')
        entity.putExtendProperty(['c': '3', 'd': '4'])
        repo.save(entity)
        
        UserEntity user = repo.findByUsername('hinex')
        assert user.getExtendProperties() != null
        assert user.getExtendProperties().startsWith("{");
        ['a':'1', 'b':'2', 'c':'3', 'd':'4'].entrySet().each { entry ->
            assert user.getExtendProperty(entry.key) == entry.value
        }
    }

}
