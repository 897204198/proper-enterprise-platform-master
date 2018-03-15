package com.proper.enterprise.platform.core.neo4j.entity;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity(label = "Neo4jDemoNode")
public class Neo4jDemoNode extends BaseNodeEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
