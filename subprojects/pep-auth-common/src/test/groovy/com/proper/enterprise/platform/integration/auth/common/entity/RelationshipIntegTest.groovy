package com.proper.enterprise.platform.integration.auth.common.entity

import com.proper.enterprise.platform.auth.common.entity.PersonEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.repository.PersonRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql

class RelationshipIntegTest extends AbstractIntegTest {

    @Autowired
    PersonRepository personRepository

    @Autowired
    UserRepository userRepository

    @Test
    @Sql('/test-data/one-person-multi-users.sql')
    public void onePersonCouldHasMultiUsers() {
        PersonEntity person = personRepository.findByName('person')
        assert person.getUserEntities().size() == 2

        UserEntity user = userRepository.findByUsername('user2')
        assert user.getPersonEntity().getName() == 'person'
    }

}
