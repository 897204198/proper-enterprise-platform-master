package com.proper.enterprise.platform.search.common.service.impl;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.repository.NativeRepository;
import com.proper.enterprise.platform.search.api.annotation.SearchConfig;
import com.proper.enterprise.platform.search.api.conf.AbstractSearchConfigs;
import com.proper.enterprise.platform.search.api.model.SearchColumn;
import com.proper.enterprise.platform.search.api.serivce.MongoDataSyncService;
import com.proper.enterprise.platform.search.common.document.SearchDocument;
import com.proper.enterprise.platform.search.common.repository.SearchMongoRepository;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "MongoDataSyncServiceImpl")
public class MongoDataSyncServiceImpl implements MongoDataSyncService {

    @Autowired
    private I18NService i18NService;

    @Autowired
    private NativeRepository nativeRepository;

    @Autowired
    SearchMongoRepository searchMongoRepository;

    @Override
    public void syncMongoFromDB() {
        searchMongoRepository.deleteAll();

        // 整理所有需要查询的表名，及表名中对应的字段
        Map<String, Object> searchConfigBeans = PEPApplicationContext.getApplicationContext().getBeansWithAnnotation(SearchConfig.class);
        Map<String, List<SearchColumn>> tableColumnMap = getTableColumnMap(searchConfigBeans);
        List<SearchDocument> documentList = new ArrayList<>();
        for (Map.Entry<String, List<SearchColumn>> tableColumnEntry: tableColumnMap.entrySet()) {
            StringBuffer querySql = new StringBuffer();
            querySql.append("SELECT ");
            // 取出该表将要查询的字段
            List<SearchColumn> searchColumnList = tableColumnEntry.getValue();
            if (searchColumnList.size() == 0) {
                throw new ErrMsgException(i18NService.getMessage("search.can.not.find.column"));
            }
            for (int j = 0; j < searchColumnList.size(); j++) {
                SearchColumn searchColumn = searchColumnList.get(j);
                String columnName = searchColumn.getColumn();
                querySql.append(columnName + ",");
            }
            querySql.deleteCharAt(querySql.length() - 1);
            String tableName = tableColumnEntry.getKey();
            querySql.append(" FROM " + tableName);
            List<Map<String, Object>> resultList = nativeRepository.executeEntityMapQuery(querySql.toString());
            Map<String, List<String>> contextFilterMap = new HashMap<>();
            for (Map<String, Object> resultMap:resultList) {
                Set<Map.Entry<String, Object>> entrySet = resultMap.entrySet();
                for (Map.Entry<String, Object> entry: entrySet) {
                    String column = entry.getKey();

                    SearchColumn searchColumnMongo = getSearchColumnMongo(searchColumnList, column);
                    if (searchColumnMongo == null) {
                        throw new ErrMsgException(i18NService.getMessage("search.can.not.find.column"));
                    }
                    // 插入mongodb
                    String value;
                    Object objValue = entry.getValue();
                    boolean isRepeat = false;
                    if (objValue != null) {
                        value = objValue.toString();
                        if (!"".equals(value.trim())) {
                            if (contextFilterMap.containsKey(value)) {
                                List<String> columnList = contextFilterMap.get(value);
                                for (String columnTemp: columnList) {
                                    if (columnTemp.equalsIgnoreCase(searchColumnMongo.getColumn())) {
                                        isRepeat = true;
                                        break;
                                    }
                                }
                                if (!isRepeat) {
                                    columnList.add(searchColumnMongo.getColumn());
                                }
                            } else {
                                List<String> columnList = new ArrayList<>();
                                columnList.add(searchColumnMongo.getColumn());
                                contextFilterMap.put(value, columnList);
                            }
                            if (!isRepeat) {
                                SearchDocument demoUserDocument = new SearchDocument();
                                demoUserDocument.setCon(value);
                                demoUserDocument.setCol(searchColumnMongo.getColumn());
                                demoUserDocument.setTab(searchColumnMongo.getTable());
                                demoUserDocument.setDes(searchColumnMongo.getDescColumn());
                                documentList.add(demoUserDocument);
                            }
                        }
                    }
                }
            }
        }
        searchMongoRepository.save(documentList);
    }

    public Map<String, List<SearchColumn>> getTableColumnMap(Map<String, Object> searchConfigBeans) {
        Map<String, List<SearchColumn>> tableColumnMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : searchConfigBeans.entrySet()) {
            AbstractSearchConfigs tempSearchConfig = (AbstractSearchConfigs) entry.getValue();
            Map<String, List<SearchColumn>> tempTableColumnMap = tempSearchConfig.getSearchTableColumnMap();
            for (Map.Entry<String, List<SearchColumn>> tempEntry: tempTableColumnMap.entrySet()) {
                String tempTableName = tempEntry.getKey();
                if (!tableColumnMap.containsKey(tempTableName)) {
                    tableColumnMap.put(tempTableName, tempEntry.getValue());
                } else {
                    List<SearchColumn> searchColumnList = tableColumnMap.get(tempTableName);
                    List<SearchColumn> tempSearchColumnList = tempEntry.getValue();
                    List<SearchColumn> addSearchColumnList = new ArrayList<>();
                    for (SearchColumn tempSearchColumn: tempSearchColumnList) {
                        boolean isRepeat = false;
                        String tempColumn = tempSearchColumn.getColumn();
                        for (SearchColumn searchColumn: searchColumnList) {
                            String column = searchColumn.getColumn();
                            if (tempColumn.equalsIgnoreCase(column)) {
                                isRepeat = true;
                                break;
                            }
                        }
                        if (!isRepeat) {
                            addSearchColumnList.add(tempSearchColumn);
                        }
                    }
                    if (addSearchColumnList.size() > 0) {
                        searchColumnList.addAll(addSearchColumnList);
                    }
                }
            }

        }
        return tableColumnMap;
    }

    public SearchColumn getSearchColumnMongo(List<SearchColumn> searchColumnList, String column) {
        for (SearchColumn searchColumn: searchColumnList) {
            if (column.equalsIgnoreCase(searchColumn.getColumn())) {
                return searchColumn;
            }
        }
        return null;
    }
}
