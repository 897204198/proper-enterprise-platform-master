package com.proper.enterprise.platform.core.neo4j.entity;

import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

@NodeEntity(label = "Neo4jDemoNodeDepthTwo")
public class Neo4jDemoNodeDepthTwo extends BaseNodeEntity {
    Neo4jDemoNodeDepthTwo() {

    }

    Neo4jDemoNodeDepthTwo(String name, Long graphId, String id, List<String> labels,
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

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
