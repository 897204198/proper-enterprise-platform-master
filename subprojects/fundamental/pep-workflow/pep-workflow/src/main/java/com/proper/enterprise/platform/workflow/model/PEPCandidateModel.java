package com.proper.enterprise.platform.workflow.model;

public class PEPCandidateModel {

    /**
     * 唯一标识
     */
    private String id;

    /**
     * 候选类型
     */
    private String type;

    /**
     * 候选类型名称
     */
    private String typeName;

    /**
     * 名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
