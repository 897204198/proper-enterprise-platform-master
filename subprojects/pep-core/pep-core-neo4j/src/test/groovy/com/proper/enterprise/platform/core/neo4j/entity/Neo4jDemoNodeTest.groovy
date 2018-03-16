package com.proper.enterprise.platform.core.neo4j.entity

import spock.lang.Specification

class Neo4jDemoNodeTest extends Specification {

    def "testGetAndSet"() {
        Neo4jDemoNode neo4jDemoNode = new Neo4jDemoNode(name, graphId, id, labels, createUserId, createTime, lastModifyUserId, lastModifyTime)
        expect:
        neo4jDemoNode.name == name
        where:
        name   | graphId | id   | labels | createUserId   | createTime   | lastModifyUserId   | lastModifyTime
        "name" | 2L      | "id" | []     | "createUserId" | "createTime" | "lastModifyUserId" | "lastModifyTime"
    }
}
