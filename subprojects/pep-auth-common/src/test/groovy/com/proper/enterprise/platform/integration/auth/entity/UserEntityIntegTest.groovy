package com.proper.enterprise.platform.integration.auth.entity

import com.proper.enterprise.platform.auth.entity.UserEntity
import com.proper.enterprise.platform.auth.repository.UserRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.core.IsEqual.equalTo

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
        
        UserEntity user = repo.findByLoginName('hinex')
        assert user.getExtendProperties() != null
        assert user.getExtendProperties().startsWith("{");
        ['a':'1', 'b':'2', 'c':'3', 'd':'4'].entrySet().each { entry ->
            assertThat user.getExtendProperty(entry.key), equalTo(entry.value)
        }
    }

}
