package com.proper.enterprise.platform.core.neo4j.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity(label = "Neo4jDemoNodeDepthOne")
public class Neo4jDemoNodeDepthOne extends BaseNodeEntity {
    Neo4jDemoNodeDepthOne() {

    }

    Neo4jDemoNodeDepthOne(String name, Long graphId, String id, List<String> labels,
                          String createUserId, String createTime, String lastModifyUserId,
                          String lastModifyTime) {
        this.setName(name);
        this.setGraphId(graphId);
        this.setId(id);
        this.setLabels(labels);
        this.setCreateUserId(createUserId);
        this.setCreateTime(createTime);
        this.setLastModifyUserId(lastModifyUserId);
        this.setLastModifyTime(lastModifyTime);
    }

    @Relationship(type = "has_depth_twos")
    private Set<Neo4jDemoNodeDepthTwo> neo4jDemoNodeDepthTwos = new HashSet<>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDeptTwo(Neo4jDemoNodeDepthTwo demoNodeDepthTwo) {
        this.neo4jDemoNodeDepthTwos.add(demoNodeDepthTwo);
    }

    public Set<Neo4jDemoNodeDepthTwo> getNeo4jDemoNodeDepthTwos() {
        return neo4jDemoNodeDepthTwos;
    }

    public void setNeo4jDemoNodeDepthTwos(Set<Neo4jDemoNodeDepthTwo> neo4jDemoNodeDepthTwos) {
        this.neo4jDemoNodeDepthTwos = neo4jDemoNodeDepthTwos;
    }
}
