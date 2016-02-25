package com.proper.enterprise.platform.integration.auth.common.entity

import com.proper.enterprise.platform.api.auth.enums.GenderType
import com.proper.enterprise.platform.auth.common.entity.PersonEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.repository.PersonRepository
import com.proper.enterprise.platform.auth.common.repository.UserRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class RelationshipIntegTest extends AbstractIntegTest {

    @Autowired
    PersonRepository personRepository

    @Autowired
    UserRepository userRepository

    @Before
    public void setUp() {
        PersonEntity person = new PersonEntity('person', 'x2348092', GenderType.MALE)
        personRepository.save(person)

        UserEntity user1 = new UserEntity('user1', 'pwd1')
        UserEntity user2 = new UserEntity('user2', 'pwd2')
        user1.setPersonEntity(person)
        user2.setPersonEntity(person)
        userRepository.save(user1)
        userRepository.save(user2)

        person.setUserEntities([user1, user2])
    }

    @Test
    public void onePersonCouldHasMultiUsers() {
        PersonEntity person = personRepository.findByName('person')
        assert person.getUserEntities().size() == 2
    }

}
