package com.proper.enterprise.platform.core.neo4j.service

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNode
import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNodeDepthOne
import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNodeDepthTwo
import com.proper.enterprise.platform.core.neo4j.repository.Neo4jDemoNodeRepository
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.neo4j.ogm.cypher.Filter
import org.neo4j.ogm.cypher.Filters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus

class Neo4jSupportTest extends AbstractNeo4jTest {
    @Autowired
    Neo4jDemoNodeService neo4jDemoNodeService
    @Autowired
    Neo4jDemoNodeRepository neo4jDemoNodeRepository

    @Test
    void "save"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        neo4jDemoNodeService.save(node1)
        expect:
        assert "node1" == neo4jDemoNodeService.findOne(node1.getId()).name
    }

    @Test
    @NoTx
    void "saveDept1"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        neo4jDemoNodeService.save(node1)
        Neo4jDemoNodeDepthOne neo4jDemoNodeDepthOne = new Neo4jDemoNodeDepthOne()
        neo4jDemoNodeDepthOne.setName("dept1")
        node1.addDeptOne(neo4jDemoNodeDepthOne)
        Neo4jDemoNodeDepthTwo neo4jDemoNodeDepthTwo = new Neo4jDemoNodeDepthTwo()
        neo4jDemoNodeDepthTwo.setName("dept2")
        neo4jDemoNodeDepthOne.addDeptTwo(neo4jDemoNodeDepthTwo)
        neo4jDemoNodeService.save(node1, 1)
        expect:
        assert "node1" == neo4jDemoNodeService.findOne(node1.getId(), 1).name
        assert "dept1" == neo4jDemoNodeService.findOne(node1.getId(), 1).getNeo4jDemoNodeDepthOnes()[0].name
        assert 0 == neo4jDemoNodeService.findOne(node1.getId(), 2)
            .getNeo4jDemoNodeDepthOnes()[0]
            .getNeo4jDemoNodeDepthTwos().size()
        Filters filters = new Filters()
        filters.add(new Filter("name", "node1"))
        assert "node1" == neo4jDemoNodeService.findAll(Neo4jDemoNode.class, filters)[0].name
    }

    @Test
    @NoTx
    void "saveAllDepth"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        
        Neo4jDemoNodeDepthOne neo4jDemoNodeDepthOne = new Neo4jDemoNodeDepthOne()
        neo4jDemoNodeDepthOne.setName("dept1")
        node1.addDeptOne(neo4jDemoNodeDepthOne)
        Neo4jDemoNodeDepthTwo neo4jDemoNodeDepthTwo = new Neo4jDemoNodeDepthTwo()
        neo4jDemoNodeDepthTwo.setName("dept2")
        neo4jDemoNodeDepthOne.addDeptTwo(neo4jDemoNodeDepthTwo)
        node1.addDeptOne(neo4jDemoNodeDepthOne)
        Neo4jDemoNode node2 = new Neo4jDemoNode()
        node2.setName("node2")
        List<Neo4jDemoNode> neo4jDemoNodeList = new ArrayList<>()
        neo4jDemoNodeList.add(node1)
        neo4jDemoNodeList.add(node2)
        neo4jDemoNodeService.save(neo4jDemoNodeList, 1)

        expect:
        assert "node1" == neo4jDemoNodeService.findOne(node1.getId()).name
        assert "node2" == neo4jDemoNodeService.findOne(node2.getId()).name
        assert "dept1" == neo4jDemoNodeService.findAll([node1.getId()])[0]
            .getNeo4jDemoNodeDepthOnes()[0].name
        assert 0 == neo4jDemoNodeService.findAll([node1.getId()], 3)[0]
            .getNeo4jDemoNodeDepthOnes()[0].getNeo4jDemoNodeDepthTwos().size()
        assert "node1" == neo4jDemoNodeService.findAll([node1.getId()])[0].name
        assert "dept1" == neo4jDemoNodeService.findAll([node1.getId()], 1)[0]
            .getNeo4jDemoNodeDepthOnes()[0].name
        assert 1 == neo4jDemoNodeService.findAll(new PageRequest(0, 1), 1).size
    }

    @Test
    void "saveAll"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        Neo4jDemoNode node2 = new Neo4jDemoNode()
        node2.setName("node2")
        List<Neo4jDemoNode> neo4jDemoNodeList = new ArrayList<>()
        neo4jDemoNodeList.add(node1)
        neo4jDemoNodeList.add(node2)
        neo4jDemoNodeService.save(neo4jDemoNodeList)
        expect:
        assert "node1" == neo4jDemoNodeService.findOne(node1.getId()).name
        assert "node2" == neo4jDemoNodeService.findOne(node2.getId()).name
    }

    @Test
    @NoTx
    void "findAll"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        neo4jDemoNodeService.save(node1)
        Neo4jDemoNodeDepthOne neo4jDemoNodeDepthOne = new Neo4jDemoNodeDepthOne()
        neo4jDemoNodeDepthOne.setName("dept1")
        node1.addDeptOne(neo4jDemoNodeDepthOne)
        Neo4jDemoNodeDepthTwo neo4jDemoNodeDepthTwo = new Neo4jDemoNodeDepthTwo()
        neo4jDemoNodeDepthTwo.setName("dept2")
        neo4jDemoNodeDepthOne.addDeptTwo(neo4jDemoNodeDepthTwo)
        neo4jDemoNodeService.save(node1, 2)
        expect:
        assert "dept1" == neo4jDemoNodeService.findAll(1)[0].getNeo4jDemoNodeDepthOnes()[0].name
        assert 0 == neo4jDemoNodeService.findAll(1)[0]
            .getNeo4jDemoNodeDepthOnes()[0]
            .getNeo4jDemoNodeDepthTwos().size()
        assert "dept2" == neo4jDemoNodeService.findAll(2)[0]
            .getNeo4jDemoNodeDepthOnes()[0]
            .getNeo4jDemoNodeDepthTwos()[0].name
        assert "dept1" == neo4jDemoNodeService.findAll()[0]
            .getNeo4jDemoNodeDepthOnes()[0].name

        assert "dept1" == neo4jDemoNodeService.findAll()[0]
            .getNeo4jDemoNodeDepthOnes()[0].name

        assert "dept1" == neo4jDemoNodeService.findAll([node1.getId()])[0]
            .getNeo4jDemoNodeDepthOnes()[0].name

        assert "dept1" == neo4jDemoNodeService.findAll([node1.getId()], 2)[0]
            .getNeo4jDemoNodeDepthOnes()[0].name

    }


    @Test
    void "findAllSort"() {
        Neo4jDemoNode neo4JDemoNode1 = new Neo4jDemoNode()
        neo4JDemoNode1.setName("saveNode1")
        neo4JDemoNode1.setCreateTime("2018-03-16 17:51:36")
        Neo4jDemoNodeDepthOne neo4jDemoNodeDepthOne = new Neo4jDemoNodeDepthOne()
        neo4jDemoNodeDepthOne.setName("dept1")
        neo4JDemoNode1.addDeptOne(neo4jDemoNodeDepthOne)
        Neo4jDemoNodeDepthTwo neo4jDemoNodeDepthTwo = new Neo4jDemoNodeDepthTwo()
        neo4jDemoNodeDepthTwo.setName("dept2")
        neo4jDemoNodeDepthOne.addDeptTwo(neo4jDemoNodeDepthTwo)
        Neo4jDemoNode neo4JDemoNode2 = new Neo4jDemoNode()
        neo4JDemoNode2.setName("saveNode2")
        neo4JDemoNode2.setCreateTime("2018-03-16 17:51:38")
        Neo4jDemoNode neo4JDemoNode3 = new Neo4jDemoNode()
        neo4JDemoNode3.setName("saveNode3")
        neo4JDemoNode3.setCreateTime("2018-03-16 17:51:40")
        neo4jDemoNodeRepository.save(neo4JDemoNode1)
        neo4jDemoNodeRepository.save(neo4JDemoNode2)
        neo4jDemoNodeRepository.save(neo4JDemoNode3)
    }

    @Test
    void "count"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        neo4jDemoNodeService.save(node1)
        Neo4jDemoNode node2 = new Neo4jDemoNode()
        node2.setName("node2")
        neo4jDemoNodeService.save(node2)
        expect:
        assert 2 == neo4jDemoNodeService.count()
    }

    @Test
    @NoTx
    void "findPage"() {
        Neo4jDemoNode neo4JDemoNode1 = new Neo4jDemoNode()
        neo4JDemoNode1.setName("saveNode1")
        neo4JDemoNode1.setCreateTime("2018-03-16 17:51:36")
        Neo4jDemoNode neo4JDemoNode2 = new Neo4jDemoNode()
        neo4JDemoNode2.setName("saveNode2")
        neo4JDemoNode2.setCreateTime("2018-03-16 17:51:38")
        Neo4jDemoNodeDepthOne neo4jDemoNodeDepthOne = new Neo4jDemoNodeDepthOne()
        neo4jDemoNodeDepthOne.setName("dept1")
        neo4JDemoNode2.addDeptOne(neo4jDemoNodeDepthOne)
        Neo4jDemoNodeDepthTwo neo4jDemoNodeDepthTwo = new Neo4jDemoNodeDepthTwo()
        neo4jDemoNodeDepthTwo.setName("dept2")
        neo4jDemoNodeDepthOne.addDeptTwo(neo4jDemoNodeDepthTwo)
        neo4jDemoNodeRepository.save(neo4JDemoNode1)
        neo4jDemoNodeRepository.save(neo4JDemoNode2)
        expect:
        def resAll = JSONUtil.parse(get('/neo4j/test/sort?pageNo=1&pageSize=1',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 2
        assert resAll.data.size() == 1
        assert resAll.getData()[0].name == "saveNode2"
    }


    @After
    void "delAll"() {
        neo4jDemoNodeService.deleteAll()
    }


}
