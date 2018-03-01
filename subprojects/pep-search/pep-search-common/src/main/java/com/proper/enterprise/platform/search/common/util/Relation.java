package com.proper.enterprise.platform.search.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 关系计算
 * author wanghp
 */
public class Relation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Relation.class);

    //节点
    private RelationMap<String, Integer> relationNodes = new RelationMap<>();

    //边
    private int[][] lines = {};

    public RelationMap<String, Integer> getRelationNodes() {
        return relationNodes;
    }

    private void addRelationNodes(String str) {
        if (!relationNodes.containsNormalValue(str)) {
            int num = relationNodes.size();
            relationNodes.put(str, num);
        }
    }

    //动态扩展二维数组
    private void extendLine() {
        int size = relationNodes.size();
        int[][] linePro = new int[size][size];
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length; j++) {
                linePro[i][j] = lines[i][j];
            }
        }
        lines = linePro;
    }

    //打印边情况
    public void printLines() {
        StringBuffer stringBuffer = new StringBuffer("\n");
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length; j++) {
                stringBuffer.append(lines[i][j] + " ");
                if (j == lines.length - 1) {
                    stringBuffer.append("\n");
                }
            }
        }
        stringBuffer.append("\n");
        LOGGER.debug(stringBuffer.toString());
    }

    //添加双向关系。包括增加节点、双关联边
    public void addRelation(String a, String b) {
        addRelationNodes(a);
        addRelationNodes(b);
        extendLine();
        lines[relationNodes.getNormalValue(a)][relationNodes.getNormalValue(b)] = 1;
        lines[relationNodes.getNormalValue(b)][relationNodes.getNormalValue(a)] = 1;
        printLines();
    }

    //计算全路径
    private List<RelationNode> routes(List<RelationNode> horizontal, String target, List<String> visited, List<RelationNode> routesList) {
        if (horizontal == null || horizontal.size() == 0) {
            return routesList;
        }
        List<RelationNode> list = new ArrayList<>();
        for (RelationNode temp : horizontal) {
            visited.add(temp.getRelationNodeId());
            int num = relationNodes.getNormalValue(temp.getRelationNodeId());
            for (int i = 0; i < lines.length; i++) {
                if (lines[num][i] == 1
                    && !horizontal.contains(relationNodes.getReverseValue(i))
                    && !visited.contains(relationNodes.getReverseValue(i))) {
                    if (relationNodes.getReverseValue(i).equals(target)) {
                        routesList.add(new RelationNode(target, temp));
                    } else {
                        list.add(new RelationNode(relationNodes.getReverseValue(i), temp));
                    }
                }
            }
        }
        routes(list, target, visited, routesList);
        return routesList;
    }

    public List<RelationNode> findRelation(String start, String end) {
        List<RelationNode> list = new ArrayList<>();
        list.add(new RelationNode(start, null));
        List<RelationNode> routes = routes(list, end, new ArrayList<>(), new ArrayList<>());
        return routes;
    }

    public void printRelation(List<RelationNode> list) {
        for (RelationNode relationNode : list) {
            printRelation(relationNode);
        }
    }

    public void printRelation(RelationNode relationNode) {
        StringBuffer stringBuffer = new StringBuffer(relationNode.getRelationNodeId() + " ");
        while (relationNode.getParentNode() != null) {
            relationNode = relationNode.getParentNode();
            stringBuffer.append(relationNode.getRelationNodeId() + " ");
        }
        LOGGER.debug(stringBuffer.toString());
    }

}
