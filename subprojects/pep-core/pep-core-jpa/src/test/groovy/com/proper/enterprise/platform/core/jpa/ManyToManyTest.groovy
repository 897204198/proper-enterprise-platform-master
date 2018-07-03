package com.proper.enterprise.platform.core.jpa

import com.proper.enterprise.platform.test.AbstractTest
import com.proper.enterprise.platform.core.jpa.entity.AOEntity
import com.proper.enterprise.platform.core.jpa.entity.COEntity
import com.proper.enterprise.platform.core.jpa.repository.AORepository
import com.proper.enterprise.platform.core.jpa.repository.CORepository
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class ManyToManyTest extends AbstractTest {

    @Autowired
    AORepository aRepo
    @Autowired
    CORepository cRepo

    def a, c1, c2

    @Before
    void setUp() {
        c1 = new COEntity('c1')
        c2 = new COEntity('c2')
        cRepo.saveAll([c1, c2])

        a = new AOEntity('a', 'pwd')
        c1.setaEntities([a])
        c2.setaEntities([a])
        a.setcEntities([c1, c2])
        aRepo.save(a)
    }

    @Test
    void saveNewRelationAndGetFromMappedByCol() {
        def result = cRepo.findAll()
        assert result[0].getaEntities() != null
        assert result[1].getaEntities() != null
    }

}
