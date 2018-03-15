package com.proper.enterprise.platform.core.neo4j.service

import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNode
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class Neo4jUpdateTest extends AbstractNeo4jTest {
    @Autowired
    Neo4jDemoNodeService neo4jDemoNodeService

    @Test
    void testUpdate() {
        Neo4jDemoNode neo4JDemoNode = new Neo4jDemoNode()
        neo4JDemoNode.setName("saveNode")
        neo4jDemoNodeService.save(neo4JDemoNode)
        assert "saveNode" == neo4jDemoNodeService.findOne(neo4JDemoNode.getId()).getName()
        neo4JDemoNode.setName("saveNode2")
        neo4jDemoNodeService.save(neo4JDemoNode)
        assert "saveNode2" == neo4jDemoNodeService.findOne(neo4JDemoNode.getId()).getName()
    }
}
