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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class SearchConditionBuilderIntegTest extends AbstractIntegTest {

    @Autowired
    ARepository aRepo

    @Autowired
    BRepository bRepo

    @Before
    public void setUp() {
        BEntity b1 = new BEntity('b10')
        BEntity b2 = new BEntity('b11')
        bRepo.save([b1, b2])

        AEntity a1 = new AEntity('u1', 'p1')
        a1.setDescription('abc')
        a1.setB(b1)

        AEntity a2 = new AEntity('u2', 'p2')
        a2.setDescription('def')
        a2.setB(b2)

        AEntity a3 = new AEntity('u3', 'p3')
        a3.setDescription('def')

        AEntity a4 = new AEntity('u4', 'p4')
        a4.setDescription('abc')

        aRepo.save([a1, a2, a3, a4])

        (12..39).each { idx ->
            def entity = new BEntity("b$idx")
            entity.attr1 = idx % 2
            bRepo.save(entity)
        }
    }

    @Test(expected = IllegalArgumentException)
    public void useErrorOp() {
        new SearchCondition('username', SearchCondition.Operator.LIKE)
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

        sc1 = new SearchCondition('description', SearchCondition.Operator.ASC)
        sc2 = new SearchCondition('username', SearchCondition.Operator.DESC)
        def sc3 = new SearchCondition('password', SearchCondition.Operator.GT, 'p1')
        result = aRepo.findAll(SearchConditionBuilder.build(sc1, sc2, sc3))
        assert 3 == result.size()
        assert result[0].username == 'u4'
        assert result[1].username == 'u3'
        assert result[2].username == 'u2'

        sc1 = new SearchCondition('b', SearchCondition.Operator.NOTNULL)
        sc2 = new SearchCondition('username', SearchCondition.Operator.DESC)
        result = aRepo.findAll(SearchConditionBuilder.build(sc1, sc2))
        assert result.size() == 2
        assert result[0].username == 'u2'
    }

    @Test
    public void relatedQuery() {
        def sc1 = new SearchCondition('b.name', 'b10')
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

    @Test
    public void pagingAndSorting() {
        def result = bRepo.findAll()
        assert 30 == result.size()
        assert result[0].name == 'b10'

        Pageable page = new PageRequest(1, 16)
        result = bRepo.findAll(page)
        assert 14 == result.size()
        assert result[0].name == 'b26'

        Sort sort = new Sort(Sort.Direction.DESC, 'name')
        result = bRepo.findAll(sort)
        assert result[0].name == 'b39'

        Sort.Order order1 = new Sort.Order('attr1')
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, 'name')
        sort = new Sort(order1, order2)
        result = bRepo.findAll(sort)
        assert result.size() == 30
        assert result[0].name == 'b11'
        assert result[2].name == 'b38'
        assert result[15].name == 'b12'
        assert result[16].name == 'b39'

        page = new PageRequest(2, 16, sort)
        result = bRepo.findAll(page)
        assert result != null
        assert result.size() == 0

        page = new PageRequest(1, 16, sort)
        result = bRepo.findAll(page)
        assert result.size() == 14
        assert result[0].name == 'b39'

        def sc1 = new SearchCondition('attr1', SearchCondition.Operator.GE, '0')
        result = bRepo.findAll(SearchConditionBuilder.build(sc1), page)
        assert result.size() == 12
        assert result[0].name == 'b35'
    }

    @Test
    public void relatedSortingQuery() {
        Sort sort = new Sort(Sort.Direction.DESC, 'b.name')
        def result = aRepo.findAll(sort)
        assert result[0].username == 'u2'

        Sort.Order order = new Sort.Order(Sort.Direction.DESC, 'b.name')
        sort = new Sort(order)
        result = aRepo.findAll(sort)
        assert result[0].username == 'u2'
    }

}
