package com.proper.enterprise.platform.core.jpa

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.core.jpa.entity.AEntity
import com.proper.enterprise.platform.core.jpa.entity.CEntity
import com.proper.enterprise.platform.core.jpa.repository.ARepository
import com.proper.enterprise.platform.core.jpa.repository.CRepository
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ManyToManyTest extends AbstractTest {

    @Autowired ARepository aRepo
    @Autowired CRepository cRepo

    def a, c1, c2

    @Before
    public void setUp() {
        c1 = new CEntity('c1')
        c2 = new CEntity('c2')
        cRepo.save([c1, c2])

        a = new AEntity('a', 'pwd')
        c1.setaEntities([a])
        c2.setaEntities([a])
        a.setcEntities([c1, c2])
        aRepo.save(a)
    }

    @Test
    public void saveNewRelationAndGetFromMappedByCol() {
        def result = cRepo.findAll()
        assert result[0].getaEntities() != null
        assert result[1].getaEntities() != null
    }

}
