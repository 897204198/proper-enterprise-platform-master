package com.proper.enterprise.platform.oopsearch.util;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;

import java.util.*;
import java.util.Map.Entry;

/**
 * Date Time Tools
 * author wanghp
 */
public class SqlInstallUtil {

    private static Relation relation = new Relation("tableRelation");

    public static String addSelectElements(List<SearchColumnModel> list) {
        RelationMap<String, Integer> relationNodes = relation.getRelationNodes();
        Map<String, Integer> times = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer("select ");
        for (SearchColumnModel scm : list) {
            int num = 0;
            if (times.containsKey(scm.getColumn())) {
                num = times.get(scm.getColumn()) + 1;
                times.put(scm.getColumn(), num);
            } else {
                times.put(scm.getColumn(), 0);
            }
            Integer i = relationNodes.getNormalValue(scm.getTable());
            stringBuffer.append(" ").append("t" + (i == null ? "" : i));
            stringBuffer.append("." + scm.getColumn());
            stringBuffer.append(" ").append("as");
            stringBuffer.append(" " + scm.getColumn() + num);
            stringBuffer.append(",");

        }
        stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public static String addTableElements(String sql, List<String> tableNames) {
        RelationMap<String, Integer> relationNodes = relation.getRelationNodes();
        if (tableNames.size() == 1) {
            return sql + " from " + tableNames.get(0) + " t where 1=1";
        }
        if (tableNames.size() == 2) {
            List<RelationNode> list = relation.findRelation(tableNames.get(0), tableNames.get(1));
            Map<String, Map<String, String>> datas = relation.getDataMap();
            RelationNode lastNode = null;
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    Integer integer = relationNodes.getNormalValue(list.get(i).getRelationNodeId());
                    StringBuffer stringBuffer = new StringBuffer(sql);
                    stringBuffer.append(" ").append("from").append(" ");
                    stringBuffer.append(list.get(i).getRelationNodeId()).append(" ");
                    stringBuffer.append(" ").append("t").append(integer == null ? "" : integer).append(" ");
                    sql = stringBuffer.toString();
                } else {
                    Map<String, String> column = datas.get(lastNode.getRelationNodeId() + ":" + list.get(i).getRelationNodeId());
                    Iterator<Entry<String, String>> entryKeyIterator = column.entrySet().iterator();
                    Entry<String, String> entry = entryKeyIterator.next();
                    String key = entry.getKey();
                    StringBuffer stringBuffer = new StringBuffer(sql);
                    stringBuffer.append("left join").append(" ").append(list.get(i).getRelationNodeId()).append(" ");
                    stringBuffer.append("t").append(relationNodes.getNormalValue(list.get(i).getRelationNodeId()));
                    stringBuffer.append(" ").append("on").append(" ");
                    stringBuffer.append("t").append(relationNodes.getNormalValue(lastNode.getRelationNodeId()));
                    stringBuffer.append(".").append(key);
                    stringBuffer.append("=").append(" ");
                    stringBuffer.append("t").append(relationNodes.getNormalValue(list.get(i).getRelationNodeId()));
                    stringBuffer.append(".").append(column.get(key)).append(" ");
                    sql = stringBuffer.toString();
                }
                lastNode = list.get(i);
            }
            sql = sql + " where 1=1 ";
        }
        return sql;
    }

    public static String addWhereDateRange(String table, String key, String[] value) {
        RelationMap<String, Integer> relationNodes = relation.getRelationNodes();
        StringBuffer stringBuffer = new StringBuffer();
        //support date auto compare.
        String date1 = value[0];
        String date2 = value[1];
        Date d1 = DateUtil.toDate(date1);
        Date d2 = DateUtil.toDate(date2);
        String start = date1;
        String end = date2;
        if (d1.getTime() > d2.getTime()) {
            start = date2;
            end = date1;
        }
        Integer i = relationNodes.getNormalValue(table);
        stringBuffer.append("t").append(i == null ? "" : i).append(".").append(key).append(" ").append(">=").append(" ");
        stringBuffer.append("'").append(start).append(" 00:00:00.000").append("'").append(" ");
        stringBuffer.append(" ").append("and").append(" ");
        stringBuffer.append("t").append(i == null ? "" : i).append(".").append(key).append(" ").append("<=").append(" ");
        stringBuffer.append("'").append(end).append(" 23:59:59.999").append("'").append(" ");
        return stringBuffer.toString();
    }

    public static String addWhereElements(String sql,
                                          String table,
                                          String logic,
                                          String key,
                                          String keyType,
                                          String operate,
                                          String innerLogic,
                                          String... value) {
        StringBuffer stringBuffer = new StringBuffer(sql);
        stringBuffer.append(" ").append(logic).append(" ");
        if (value.length > 1) {
            stringBuffer.append("(");
            if (innerLogic.equals("range")) {
                if (keyType.equals("date")) {
                    stringBuffer.append(addWhereDateRange(table, key, value));
                }
            } else {
                for (int i = 0; i < value.length; i++) {
                    stringBuffer.append(addElementWithoutLogic("", table, key, keyType, operate, value[i]));
                    if (i < value.length - 1) {
                        stringBuffer.append(" ").append(innerLogic).append(" ");
                    }
                }
            }
            stringBuffer.append(")");
        } else {
            stringBuffer.append(addElementWithoutLogic("", table, key, keyType, operate, value[0]));
        }
        return stringBuffer.toString();
    }

    private static String getSeparator(String keyType, String operate, String director) {
        Map<String, String> map = new HashMap<>();
        //op
        map.put("likel", "'%");
        map.put("liker", "%'");
        map.put("=l", "'");
        map.put("=r", "'");
        //type
        map.put("num", "");
        if ("num".equals(keyType)) {
            return map.get(keyType);
        } else {
            return map.get(operate + director);
        }
    }

    private static String addElementWithoutLogic(String sql, String table, String key, String keyType, String operate, String value) {
        RelationMap<String, Integer> relationNodes = relation.getRelationNodes();
        Integer i = relationNodes.getNormalValue(table);
        operate = operate == null ? getDefaultTypeOperate(keyType) : operate;
        StringBuffer stringBuffer = new StringBuffer(sql);
        stringBuffer.append(" ").append("t").append(i == null ? "" : i).append(".").append(key).append(" ");
        stringBuffer.append(" ").append(operate).append(" ");
        stringBuffer.append(getSeparator(keyType, operate, "l"));
        stringBuffer.append(value);
        stringBuffer.append(getSeparator(keyType, operate, "r"));
        return stringBuffer.toString();
    }

    private static String getDefaultTypeOperate(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("text", "like");
        map.put("num", "=");
        map.put("date", "like");
        return map.get(type) == null ? "like" : map.get(type);
    }

}
