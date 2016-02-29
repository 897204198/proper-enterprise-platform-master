package com.proper.enterprise.platform.integration.auth.common.entity

import com.proper.enterprise.platform.api.auth.model.Position
import com.proper.enterprise.platform.auth.common.entity.OrganizationEntity
import com.proper.enterprise.platform.auth.common.entity.PersonEntity
import com.proper.enterprise.platform.auth.common.entity.PositionEntity
import com.proper.enterprise.platform.auth.common.entity.UserEntity
import com.proper.enterprise.platform.auth.common.repository.OrganizationRepository
import com.proper.enterprise.platform.auth.common.repository.PersonRepository
import com.proper.enterprise.platform.auth.common.repository.PositionRepository
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

    @Autowired
    PositionRepository positionRepository

    @Autowired
    OrganizationRepository organizationRepository

    @Test
    @Sql('/test-data/one-person-multi-users.sql')
    public void onePersonCouldHasMultiUsers() {
        PersonEntity person = personRepository.findByName('person')
        assert person.getUserEntities().size() == 2

        UserEntity user = userRepository.findByUsername('user2')
        assert user.getPersonEntity().getName() == 'person'
    }

    @Test
    @Sql('/test-data/many-persons-many-positions.sql')
    public void manyToManyBetweenPersonAndPosition() {
        PersonEntity person = personRepository.findByName('person1')
        assert person.getPositionEntities().size() == 3

        PositionEntity position = positionRepository.findByName('position2')
        assert position.getPersonEntities().size() == 2
    }

    @Test
    @Sql('/test-data/many-persons-many-positions.sql')
    public void getParentPosition() {
        PositionEntity position = positionRepository.findByName('position3')
        Position parent = position.getParent()
        assert parent.name == 'position2'

        Position grand = parent.getParent()
        assert grand.name == 'position1'

        assert grand.getParent() == null
    }

    @Test
    @Sql('/test-data/positions-belong-to-org.sql')
    public void positionsBelongToOrg() {
        OrganizationEntity org = organizationRepository.findByName('org1')
        assert org.getPositionEntities().size() == 2

        PositionEntity position = positionRepository.findByName('position3')
        assert position.getBelongOrg().name == 'org2'
    }

    @Test
    @Sql('/test-data/positions-belong-to-org.sql')
    public void getParentOrg() {
        OrganizationEntity org = organizationRepository.findByName('org3')
        def parent = org.getParent()
        assert parent.name == 'org2'

        def grand = parent.getParent()
        assert grand.name == 'org1'

        assert grand.getParent() == null
    }

}
