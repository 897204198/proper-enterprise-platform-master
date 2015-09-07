package com.proper.enterprise.platform.integration.webapp.dal

import com.proper.enterprise.platform.core.repository.SearchCondition
import com.proper.enterprise.platform.core.repository.SearchConditionBuilder
import com.proper.enterprise.platform.integration.webapp.dal.entity.AEntity
import com.proper.enterprise.platform.integration.webapp.dal.entity.BEntity
import com.proper.enterprise.platform.integration.webapp.dal.repository.ARepository
import com.proper.enterprise.platform.integration.webapp.dal.repository.BRepository
import com.proper.enterprise.platform.test.integration.AbstractIntegTest
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class SearchConditionBuilderIntegTest extends AbstractIntegTest {

    @Autowired
    ARepository aRepo

    @Autowired
    BRepository bRepo

    @Before
    public void setUp() {
        BEntity b1 = new BEntity('b1')
        BEntity b2 = new BEntity('b2')
        bRepo.save([b1, b2])

        AEntity a1 = new AEntity('u1', 'p1')
        a1.setB(b1)

        AEntity a2 = new AEntity('u2', 'p2')
        a2.setB(b2)

        AEntity a3 = new AEntity('u3', 'p3')

        AEntity a4 = new AEntity('u4', 'p4')

        aRepo.save([a1, a2, a3, a4])
    }

    @Test
    public void singleEntityQuery() {
        def sc1 = new SearchCondition('username', SearchCondition.Operator.LIKE, 'u')
        def sc2 = new SearchCondition('password', 'p3')
        def result = aRepo.findAll(SearchConditionBuilder.build(sc1, sc2))
        assert 1 == result.size()
        assert result[0].username == 'u3'

        sc1 = new SearchCondition('username', SearchCondition.Operator.GT, 'u2')
        sc2 = new SearchCondition('username', SearchCondition.Operator.LT, 'u4')
        result = aRepo.findAll(SearchConditionBuilder.build(sc1, sc2))
        assert 1 == result.size()
        assert result[0].username == 'u3'

        sc1 = new SearchCondition('username', SearchCondition.Operator.GE, 'u2')
        sc2 = new SearchCondition('username', SearchCondition.Operator.LE, 'u4')
        result = aRepo.findAll(SearchConditionBuilder.build(sc1, sc2))
        assert 3 == result.size()
    }

    @Test
    public void relatedQuery() {
        def sc1 = new SearchCondition('b.name', 'b1')
        def result = aRepo.findAll(SearchConditionBuilder.build(sc1))
        assert 1 == result.size()
        assert result[0].username == 'u1'

        sc1 = new SearchCondition('b.name', SearchCondition.Operator.LIKE, 'b')
        result = aRepo.findAll(SearchConditionBuilder.build(sc1))
        assert 2 == result.size()

        def sc2 = new SearchCondition('password', 'p2')
        result = aRepo.findAll(SearchConditionBuilder.build(sc1, sc2))
        assert 1 == result.size()
        assert result[0].username == 'u2'
    }

}
