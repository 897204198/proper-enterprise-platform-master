package com.proper.enterprise.platform.core.neo4j.service

import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNode
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class Neo4jSupportTest extends AbstractNeo4jTest {
    @Autowired
    Neo4jDemoNodeService neo4jDemoNodeService

    @Test
    void "save"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        neo4jDemoNodeService.save(node1)
        expect:
        assert "node1" == neo4jDemoNodeService.findOne(node1.getId()).name
    }

    @Test
    void "saveAll"() {
        Neo4jDemoNode node1 = new Neo4jDemoNode()
        node1.setName("node1")
        Neo4jDemoNode node2 = new Neo4jDemoNode()
        node2.setName("node2")
        List<Neo4jDemoNode> neo4jDemoNodeList=new ArrayList<>()
        neo4jDemoNodeList.add(node1)
        neo4jDemoNodeList.add(node2)
        neo4jDemoNodeService.save(neo4jDemoNodeList)
        expect:
        assert "node1" == neo4jDemoNodeService.findOne(node1.getId()).name
        assert "node2" == neo4jDemoNodeService.findOne(node2.getId()).name
    }
}
