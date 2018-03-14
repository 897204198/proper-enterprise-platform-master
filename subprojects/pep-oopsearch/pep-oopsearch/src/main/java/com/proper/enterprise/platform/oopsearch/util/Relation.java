package com.proper.enterprise.platform.oopsearch.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * 关系计算
 * author wanghp
 */
public class Relation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Relation.class);

    public Relation(String relationName) {
        this.relationName = relationName;
        loadingLines();
    }

    //关系名称
    private String relationName;

    //边
    private int[][] lines = {};

    /*
        加载全部节点关系
    */
    private void loadingLines() {
        addRelations();
        int size = relationNodes.size();
        lines = new int[size][size];
        Set<String> set = dataMap.keySet();
        for (String temp : set) {
            String[] str = temp.split(":");
            lines[relationNodes.getNormalValue(str[0])][relationNodes.getNormalValue(str[1])] = 1;
        }
    }

    //节点
    private RelationMap<String, Integer> relationNodes = new RelationMap<>();

    //扩展字段
    private Map<String, Map<String, String>> dataMap = new HashMap<>();

    public Map<String, Map<String, String>> getDataMap() {
        return dataMap;
    }

    public RelationMap<String, Integer> getRelationNodes() {
        return relationNodes;
    }

    /*
            添加关系节点
        */
    private void addRelationNode(String nodeId) {
        if (!relationNodes.containsNormalValue(nodeId)) {
            int num = relationNodes.size();
            relationNodes.put(nodeId, num);
        }
    }

    /*
         添加扩展字段，选填。如果需要请成对传入
    */
    private void addDatas(String nodeA, String nodeB, String[] datas) {
        Map<String, String> forward = new HashMap<>();
        Map<String, String> reverse = new HashMap<>();
        if (datas.length > 0 && datas.length % 2 == 0) {
            forward.put(datas[0], datas[1]);
            reverse.put(datas[1], datas[0]);
        }
        dataMap.put(nodeA + ":" + nodeB, forward);
        dataMap.put(nodeB + ":" + nodeA, reverse);
    }

    private void addRelations() {
        ApplicationContext context = PEPApplicationContext.getApplicationContext();
        List<Map<String, Object>> configInfo = (List<Map<String, Object>>) context.getBean(relationName);
        for (Map<String, Object> map : configInfo) {
            String nodeA = (String) map.get("nodeA");
            String nodeB = (String) map.get("nodeB");
            String[] datas = (String[]) map.get("datas");
            addRelation(nodeA, nodeB, datas);
        }
    }

    /*
        添加节点、双向关系、扩展字段
     */
    private void addRelation(String nodeA, String nodeB, String... datas) {
        addRelationNode(nodeA);
        addRelationNode(nodeB);
        addDatas(nodeA, nodeB, datas);
    }

    /*
        递归计算最短路径
    */
    private List<RelationNode> routes(List<RelationNode> horizontal, String target, List<String> visited, List<RelationNode> routesList, int level) {
        if (horizontal == null || horizontal.size() == 0) {
            return routesList;
        }
        level++;
        List<RelationNode> list = new ArrayList<>();
        for (RelationNode temp : horizontal) {
            visited.add(temp.getRelationNodeId());
            int num = relationNodes.getNormalValue(temp.getRelationNodeId());
            for (int i = 0; i < lines.length; i++) {
                if (lines[num][i] == 1
                    && !horizontal.contains(relationNodes.getReverseValue(i))
                    && !visited.contains(relationNodes.getReverseValue(i))) {
                    if (relationNodes.getReverseValue(i).equals(target)) {
                        if (routesList.size() > 0) {
                            if (routesList.get(0).getLevel() > level) {
                                routesList.add(new RelationNode(target, temp, level));
                            }
                        } else {
                            routesList.add(new RelationNode(target, temp, level));
                        }
                    } else {
                        list.add(new RelationNode(relationNodes.getReverseValue(i), temp, level));
                    }
                }
            }
        }
        routes(list, target, visited, routesList, level);
        return routesList;
    }

    /*
        返回最短路径，如果有多个只取第一个
    */
    public List<RelationNode> findRelation(String start, String end) {
        List<RelationNode> list = new ArrayList<>();
        list.add(new RelationNode(start, null, 1));
        List<RelationNode> routes = routes(list, end, new ArrayList<>(), new ArrayList<>(), 1);
        if (routes != null && routes.size() > 0) {
            return openRelation(routes.get(0));
        }
        return routes;
    }

    /*
        展开路径
    */
    private List<RelationNode> openRelation(RelationNode relationNode) {
        List<RelationNode> list = new ArrayList<>();
        list.add(relationNode);
        while (relationNode.getParentNode() != null) {
            relationNode = relationNode.getParentNode();
            list.add(relationNode);
        }
        Collections.reverse(list);
        return list;
    }


}
