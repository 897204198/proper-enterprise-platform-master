package com.proper.enterprise.platform.core.neo4j.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity(label = "Neo4jDemoNode")
public class Neo4jDemoNode extends BaseNodeEntity {
    Neo4jDemoNode() {

    }

    Neo4jDemoNode(String name, Long graphId, String id, List<String> labels,
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

    @Relationship(type = "has_depth_one")
    private Set<Neo4jDemoNodeDepthOne> neo4jDemoNodeDepthOnes = new HashSet<>();

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Neo4jDemoNodeDepthOne> getNeo4jDemoNodeDepthOnes() {
        return neo4jDemoNodeDepthOnes;
    }

    public void setNeo4jDemoNodeDepthOnes(Set<Neo4jDemoNodeDepthOne> neo4jDemoNodeDepthOnes) {
        this.neo4jDemoNodeDepthOnes = neo4jDemoNodeDepthOnes;
    }

    public void addDeptOne(Neo4jDemoNodeDepthOne demoNodeDepthOne) {
        this.neo4jDemoNodeDepthOnes.add(demoNodeDepthOne);
    }
}
