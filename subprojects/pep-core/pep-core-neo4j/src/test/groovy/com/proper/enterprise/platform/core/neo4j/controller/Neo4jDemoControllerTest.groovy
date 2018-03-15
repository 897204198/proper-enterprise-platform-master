package com.proper.enterprise.platform.core.neo4j.controller

import com.proper.enterprise.platform.core.entity.DataTrunk
import com.proper.enterprise.platform.core.neo4j.entity.Neo4jDemoNode
import com.proper.enterprise.platform.core.neo4j.service.Neo4jDemoNodeService
import com.proper.enterprise.platform.core.utils.JSONUtil
import com.proper.enterprise.platform.test.AbstractNeo4jTest
import com.proper.enterprise.platform.test.annotation.NoTx
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

class Neo4jDemoControllerTest extends AbstractNeo4jTest {
    @Autowired
    private Neo4jDemoNodeService neo4jDemoNodeService

    @Test
    @NoTx
    void testPage() {
        neo4jDemoNodeService.deleteAll()
        List<Neo4jDemoNode> list = new ArrayList<>()
        Neo4jDemoNode neo4JDemoNode1 = new Neo4jDemoNode()
        neo4JDemoNode1.setName("saveNode1")
        Neo4jDemoNode neo4JDemoNode2 = new Neo4jDemoNode()
        neo4JDemoNode2.setName("saveNode2")
        list.add(neo4JDemoNode1)
        list.add(neo4JDemoNode2)
        neo4jDemoNodeService.save(list)
        def resAll = JSONUtil.parse(get('/neo4j/test?pageNo=1&pageSize=1',
            HttpStatus.OK).getResponse().getContentAsString(), DataTrunk.class)
        assert resAll.count == 2
        assert resAll.data.size() == 1
        def err = get('/neo4j/test', HttpStatus.BAD_REQUEST)
        assert "missing pageNo to buildPage" == err.getResponse().getContentAsString()
    }

    @After
    void delAll() {
        neo4jDemoNodeService.deleteAll()
    }
}
