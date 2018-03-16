package com.proper.enterprise.platform.core.neo4j.service

import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNode
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class Neo4jCreateTest extends AbstractNeo4jTest {
    @Autowired
    Neo4jDemoNodeService neo4jDemoNodeService

    @Test
    void testSave() {
        Neo4jDemoNode neo4JDemoNode = new Neo4jDemoNode()
        neo4JDemoNode.setName("saveNode")
        neo4jDemoNodeService.deleteAll()
        neo4jDemoNodeService.save(neo4JDemoNode)
        assert "saveNode" == neo4jDemoNodeService.findOne(neo4JDemoNode.getId()).getName()
        Neo4jDemoNode neo4jDemoNode2=new Neo4jDemoNode()
        neo4jDemoNodeService.save(neo4jDemoNode2,1)
    }

    @Test
    void testSaveAll() {
        List<Neo4jDemoNode> list = new ArrayList<>()
        Neo4jDemoNode neo4JDemoNode1 = new Neo4jDemoNode()
        neo4JDemoNode1.setName("saveNode1")
        Neo4jDemoNode neo4JDemoNode2 = new Neo4jDemoNode()
        neo4JDemoNode2.setName("saveNode2")
        list.add(neo4JDemoNode1)
        list.add(neo4JDemoNode2)
        neo4jDemoNodeService.save(list)
        assert 2 == neo4jDemoNodeService.findAll().size()
    }
}
