package com.proper.enterprise.platform.oopsearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.oopsearch.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import com.proper.enterprise.platform.oopsearch.util.DateTimeUtil;
import com.proper.enterprise.platform.oopsearch.util.SqlInstallUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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

    private AbstractSearchConfigs getSearchConfigs(String moduleName) {
        ApplicationContext context = PEPApplicationContext.getApplicationContext();
        AbstractSearchConfigs searchConfig = (AbstractSearchConfigs) context.getBean(moduleName);
        return searchConfig;
    }

    private List<SearchColumnModel> getAllColumns(AbstractSearchConfigs searchConfig) {
        List<SearchColumnModel> list = new ArrayList<>();
        List<String> tableNames = searchConfig.getTableNameList();
        for (String temp : tableNames) {
            list.addAll(searchConfig.getSearchColumnListByTable(temp));
        }
        return list;
    }

    String installSql(JsonNode query, String moduleName) {
        AbstractSearchConfigs searchConfig = getSearchConfigs(moduleName);
        List<String> tableNames = searchConfig.getTableNameList();
        List<SearchColumnModel> columns = getAllColumns(searchConfig);
        String sql = SqlInstallUtil.addSelectElements(columns);
        sql = SqlInstallUtil.addTableElements(sql, tableNames);
        try {
            String logic = "";
            for (int i = 0; i < query.size(); i++) {
                JsonNode jn = query.get(i);
                String key = jn.findValue("key").asText();
                String table = jn.findValue("table").asText();
                boolean isOperater = key.contains("or") || key.contains("and");
                if (isOperater) {
                    logic = key;
                } else {
                    String value = jn.findValue("value").asText();
                    String type = searchConfig.getSearchTableColumn().get(table).get(key).getType();
                    JsonNode jnOperate = jn.findValue("operate");
                    if (StringUtil.isEmpty(logic)) {
                        logic = "and";
                    }
                    if (type.equals("date")) {
                        sql = sql + installDate(table, key, logic, jnOperate.asText(), value, type);
                    } else {
                        sql = SqlInstallUtil.addWhereElements(sql, table, logic, key, type, jnOperate.asText(), "", value);
                    }
                    logic = "";
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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

    private String installDate(String table, String key, String logic, String operate, String date, String type) {
        try {
            String ch = getChLogicFromDate(date);
            if (ch != null) {
                //CH many date
                String[] elements = date.split(ch);
                return SqlInstallUtil.addWhereElements("", table, logic, key, type, operate, getEnLogic(ch), elements);
            } else {
                //Normal date
                Date newDate = DateUtil.toDate(date);
                String newStrDate = DateUtil.toString(newDate, "yyyy-MM-dd");
                return SqlInstallUtil.addWhereElements("", table, logic, key, type, operate, "", newStrDate);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            //Special date
            try {
                String log = e.getMessage();
                if (log.contains("too short")) {
                    //Partly date
                    return SqlInstallUtil.addWhereElements("", table, logic, key, type, "like", "", date);
                } else if (log.contains("Invalid format") && !log.contains("too short")) {
                    //CH phrase date
                    String ruler = date.substring(0, date.length() - 1);
                    String element = date.substring(1, date.length());
                    int position = DateTimeUtil.getTimeAxis(ruler);
                    String subject = DateTimeUtil.getDateSubject(element);
                    if ("day".equals(subject)) {
                        //single
                        return SqlInstallUtil.addWhereElements("", table, logic, key, type, "like", "", DateTimeUtil.day(position));
                    } else {
                        //range
                        Method method = DateTimeUtil.class.getMethod(subject + "Range", Integer.class);
                        Map<String, String> range = (Map<String, String>) method.invoke(null, position);
                        return SqlInstallUtil.addWhereElements("", table, logic, key, type, "", "range", range.get("S"), range.get("E"));
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
