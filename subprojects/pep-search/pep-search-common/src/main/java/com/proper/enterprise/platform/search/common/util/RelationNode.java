package com.proper.enterprise.platform.search.common.util;

/**
 * 层级关系节点
 * author wanghp
 */
public class RelationNode {
    private String relationNodeId;
    private RelationNode parentNode;
    private Integer level;

    public RelationNode(){
    }

    public RelationNode(String relationNodeId, RelationNode parentNode, Integer level) {
        this.relationNodeId = relationNodeId;
        this.parentNode = parentNode;
        this.level = level;
    }

    public String getRelationNodeId() {
        return relationNodeId;
    }

    public RelationNode getParentNode() {
        return parentNode;
    }

    public Integer getLevel() {
        return level;
    }

}
