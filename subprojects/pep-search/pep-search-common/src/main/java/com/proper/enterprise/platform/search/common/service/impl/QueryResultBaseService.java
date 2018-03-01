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
        String sql = SqlInstallUtil.addSelectElements(set);
        sql = SqlInstallUtil.addTableElements(sql, tableName);
        String logic = "";
        for (int i = 0; i < root.size(); i++) {
            JsonNode jn = root.get(i);
            String key = jn.findValue("key").asText();
            boolean isOperater = key.contains("or") || key.contains("and");
            if (isOperater) {
                logic = key;
            } else {
                String value = jn.findValue("value").asText();
                String type = columnMap.get(key);
                JsonNode jnOperate = jn.findValue("operate");
                if (StringUtil.isEmpty(logic)) {
                    logic = "and";
                }
                if (type.equals("date")) {
                    sql = sql + installDate(key, logic, jnOperate.asText(), value, type);
                } else {
                    sql = SqlInstallUtil.addWhereElements(sql, logic, key, type, jnOperate.asText(), "", value);
                }
                logic = "";
            }
        }
        return sql;
    }

    private String getChLogicFromDate(String str) {
        if (str.contains(i18NService.getMessage("search.or"))) {
            return i18NService.getMessage("search.or");
        } else if (str.contains(i18NService.getMessage("search.till"))) {
            return i18NService.getMessage("search.till");
        } else {
            return null;
        }
    }

    private String getEnLogic(String str) {
        Map<String, String> map = new HashMap<>();
        map.put(i18NService.getMessage("search.or"), "or");
        map.put(i18NService.getMessage("search.till"), "range");
        return map.get(str);
    }

    private String installDate(String key, String logic, String operate, String date, String type) {
        try {
            String ch = getChLogicFromDate(date);
            if (ch != null) {
                //CH many date
                String[] elements = date.split(ch);
                return SqlInstallUtil.addWhereElements("", logic, key, type, operate, getEnLogic(ch), elements);
            } else {
                //Normal date
                Date newDate = DateUtil.toDate(date);
                String newStrDate = DateUtil.toString(newDate, "yyyy-MM-dd");
                return SqlInstallUtil.addWhereElements("", logic, key, type, operate, "", newStrDate);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            //Special date
            try {
                String log = e.getMessage();
                if (log.contains("too short")) {
                    //Partly date
                    return SqlInstallUtil.addWhereElements("", logic, key, type, "like", "", date);
                } else if (log.contains("Invalid format") && !log.contains("too short")) {
                    //CH phrase date
                    String ruler = date.substring(0, date.length() - 1);
                    String element = date.substring(1, date.length());
                    int position = DateTimeUtil.getTimeAxis(ruler);
                    String subject = DateTimeUtil.getDateSubject(element);
                    if ("day".equals(subject)) {
                        //single
                        return SqlInstallUtil.addWhereElements("", logic, key, type, "like", "", DateTimeUtil.day(position));
                    } else {
                        //range
                        Method method = DateTimeUtil.class.getMethod(subject + "Range", Integer.class);
                        Map<String, String> range = (Map<String, String>) method.invoke(null, position);
                        return SqlInstallUtil.addWhereElements("", logic, key, type, "", "range", range.get("S"), range.get("E"));
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
