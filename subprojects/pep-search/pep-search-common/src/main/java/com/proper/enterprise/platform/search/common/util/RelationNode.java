package com.proper.enterprise.platform.search.common.util;

/**
 * 层级关系节点
 * author wanghp
 */
public class RelationNode {
    private String relationNodeId;
    private RelationNode parentNode;

    public RelationNode(String relationNodeId, RelationNode parentNode) {
        this.relationNodeId = relationNodeId;
        this.parentNode = parentNode;
    }

    public String getRelationNodeId() {
        return relationNodeId;
    }

    public RelationNode getParentNode() {
        return parentNode;
    }

}
