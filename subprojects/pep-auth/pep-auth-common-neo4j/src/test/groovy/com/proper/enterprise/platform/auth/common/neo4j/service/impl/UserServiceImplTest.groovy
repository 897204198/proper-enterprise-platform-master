package com.proper.enterprise.platform.auth.common.neo4j.service.impl

import com.proper.enterprise.platform.api.auth.service.UserService
import com.proper.enterprise.platform.auth.common.neo4j.entity.UserNodeEntity
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImplTest extends AbstractNeo4jTest {

    @Autowired
    UserService userService

    @Test
    void testUpdate() {
        UserNodeEntity userNodeEntity = new UserNodeEntity()
        userNodeEntity.setName('user')
        userService.save(userNodeEntity)
        Map<String, Object> userMap = new HashMap<>()
        userMap.put('id', userNodeEntity.getId())
        userMap.put('name', 'new name')
        userMap.put('email', 'eee@dsf.com')
        userMap.put('phone', '12345664')
        userMap.put('password', '1111111')
        userMap.put('enable', false)
        UserNodeEntity userNodeEntity1 = userService.updateByUser(userMap)
        assert userNodeEntity1.getName() == 'new name'
    }

}
