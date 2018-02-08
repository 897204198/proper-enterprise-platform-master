package com.proper.enterprise.platform.search.common.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.search.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.search.api.model.SearchColumn;
import com.proper.enterprise.platform.search.common.util.DateTimeUtil;
import com.proper.enterprise.platform.search.common.util.SqlInstallUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.*;

/**
 * NativeSQL Concat
 * author wanghp
 */
public class QueryResultBaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryResultBaseService.class);

    @Autowired
    private I18NService i18NService;


    private Map<String, String> getFieldsByTableName(AbstractSearchConfigs searchConfig, String tableName) {
        List<SearchColumn> searchColumnList = searchConfig.getSearchColumnListByTable(tableName);
        Map<String, String> columnMap = new HashMap<>();
        for (SearchColumn temp : searchColumnList) {
            columnMap.put(temp.getColumn(), temp.getType());
        }
        return columnMap;
    }

    String installSql(AbstractSearchConfigs searchConfig, JsonNode root, String tableName) {
        Map<String, String> columnMap = getFieldsByTableName(searchConfig, tableName);
        Set<String> set = columnMap.keySet();
        String sql = SqlInstallUtil.addSelectElements("", set);
        sql = SqlInstallUtil.addTableElements(sql, tableName);
        String conditionOperate = "";
        for (int i = 0; i < root.size(); i++) {
            JsonNode jn = root.get(i);
            String key = jn.findValue("key").asText();
            boolean isOperater = key.contains("or") || key.contains("and");
            if (isOperater) {
                conditionOperate = key;
            } else {
                String value = jn.findValue("value").asText();
                String type = columnMap.get(key);
                JsonNode jnOperate = jn.findValue("operate");
                if (StringUtil.isEmpty(conditionOperate)) {
                    conditionOperate = "and";
                }
                if (type.equals("date")) {
                    String operate = jnOperate == null ? "like" : jnOperate.asText();
                    value = installDate(key, operate, value);
                    if (StringUtil.isEmpty(value)) {
                        continue;
                    }
                    sql = sql + " " + conditionOperate + " " + value;
                } else if (type.equals("num")) {
                    String operate = jnOperate == null ? "=" : jnOperate.asText();
                    sql = SqlInstallUtil.addWhereElementInt(sql, conditionOperate, key, operate, value);
                } else {
                    String operate = jnOperate == null ? "like" : jnOperate.asText();
                    sql = SqlInstallUtil.addWhereElement(sql, conditionOperate, key, operate, value);
                }
                conditionOperate = "";
            }
        }
        return sql;
    }

    private String installDate(String key, String operate, String date) {
        try {
            if (date.contains(i18NService.getMessage("search.or"))) {
                String[] orArray = date.split(i18NService.getMessage("search.or"));
                //TODO
                return SqlInstallUtil.addWhereElements("", key, operate, orArray);
            } else if (date.contains(i18NService.getMessage("search.till"))) {
                String[] toArray = date.split(i18NService.getMessage("search.till"));
                return SqlInstallUtil.addRangeElements("", key, toArray[0], toArray[1]);
            }
            Date newDate = DateUtil.toDate(date);
            String newStrDate = DateUtil.toString(newDate, "yyyy-MM-dd");
            return SqlInstallUtil.addWhereElement("", "", key, operate, newStrDate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                String log = e.getMessage();
                if (log.contains("too short")) {
                    return SqlInstallUtil.addWhereElement("", "", key, "like", date);
                } else if (log.contains("Invalid format")) {
                    String ruler = date.substring(0, date.length() - 1);
                    String element = date.substring(1, date.length());
                    int position = DateTimeUtil.getTimeAxis(ruler, i18NService);
                    String subject = DateTimeUtil.getDateSubject(element, i18NService);
                    if ("day".equals(subject)) {
                        return SqlInstallUtil.addWhereElement("", "", key, "like", DateTimeUtil.day(position));
                    } else {
                        Method method = DateTimeUtil.class.getMethod(subject + "Range", Integer.class);
                        Map<String, String> range = (Map<String, String>) method.invoke(null, position);
                        return SqlInstallUtil.addRangeElements("", key, range.get("S"), range.get("E"));
                    }
                } else {
                    return null;
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                return null;
            }
        }
    }

}
